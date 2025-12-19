// presentation/ui/screens/quickpay/QuickPayViewModel.kt
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

data class QuickPayUiState(
    val amount: String = "",
    val isLoading: Boolean = false,
    val message: String? = null,
    val paymentMethod: PaymentMethod = PaymentMethod.TAP_TO_PAY
)

class QuickPayViewModel : ViewModel() {

    private val performPayment =
        AppContainer.performPaymentUseCase

    private val _ui = MutableStateFlow(QuickPayUiState())
    val ui: StateFlow<QuickPayUiState> = _ui

    init {
        viewModelScope.launch {
            AppContainer.paymentCompletedTrigger.collect {
                reset()
            }
        }
    }

    fun setPaymentMethod(method: PaymentMethod) {
        _ui.update { it.copy(paymentMethod = method) }
    }

    fun setAmount(value: String) {
        _ui.update { it.copy(amount = value, message = null) }
    }

    fun reset() {
        _ui.value = QuickPayUiState()
    }

    fun onPayClicked(
        activity: ComponentActivity,
        paymentLauncher: ActivityResultLauncher<Intent>,
        currency: String = "EUR",
        method: PaymentMethod = _ui.value.paymentMethod
    ) {
        val amount = _ui.value.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _ui.update { it.copy(message = "Enter a valid amount") }
            return
        }

        _ui.update { it.copy(isLoading = true, message = null) }

        viewModelScope.launch {
            try {
                performPayment(
                    activity = activity,
                    paymentLauncher = paymentLauncher,
                    amount = amount,
                    currency = currency,
                    method = method
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

