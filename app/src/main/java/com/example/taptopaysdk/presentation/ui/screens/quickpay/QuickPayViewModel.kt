package com.example.taptopaysdk.presentation.ui.screens.quickpay

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taptopaysdk.di.AppContainer
import com.example.taptopaysdk.domain.model.PaymentMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

data class QuickPayUiState(
    val cents: Int = 0,
    val isLoading: Boolean = false,
    val message: String? = null,
    val paymentMethod: PaymentMethod = PaymentMethod.TAP_TO_PAY,
    val currencyCode: String = "EUR",
    val locale: Locale = Locale.GERMANY
)

class QuickPayViewModel : ViewModel() {

    private val performPayment = AppContainer.performPaymentUseCase

    private val _ui = MutableStateFlow(QuickPayUiState())
    val ui: StateFlow<QuickPayUiState> = _ui

    init {
        viewModelScope.launch {
            // Automatically reset the screen when a payment completes successfully
            AppContainer.paymentCompletedTrigger.collect {
                reset()
            }
        }
    }

    fun setPaymentMethod(method: PaymentMethod) {
        _ui.update { it.copy(paymentMethod = method) }
    }

    fun setCurrency(code: String, locale: Locale) {
        _ui.update { it.copy(currencyCode = code, locale = locale) }
    }

    fun setCents(value: Int) {
        _ui.update { it.copy(cents = value) }
    }

    fun reset() {
        _ui.value = QuickPayUiState()
    }

    fun onPayClicked(
        activity: ComponentActivity,
        paymentLauncher: ActivityResultLauncher<Intent>
    ) {
        val currentState = _ui.value

        // Validation
        if (currentState.cents <= 0) {
            _ui.update { it.copy(message = "Enter a valid amount") }
            return
        }

        // Convert cents to double for the Nexo request (e.g., 1050 -> 10.50)
        val amount = currentState.cents / 100.0

        _ui.update { it.copy(isLoading = true, message = null) }

        viewModelScope.launch {
            try {
                performPayment(
                    activity = activity,
                    paymentLauncher = paymentLauncher,
                    amount = amount,
                    currency = currentState.currencyCode,
                    method = currentState.paymentMethod
                )
                _ui.update {
                    it.copy(isLoading = false, message = "Ready for payment…")
                }
            } catch (e: Exception) {
                _ui.update {
                    it.copy(isLoading = false, message = "Failed: ${e.message}")
                }
            }
        }
    }
}