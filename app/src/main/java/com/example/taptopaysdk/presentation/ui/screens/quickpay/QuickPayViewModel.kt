// presentation/ui/screens/quickpay/QuickPayViewModel.kt
package com.example.taptopaysdk.presentation.ui.screens.quickpay

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taptopaysdk.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuickPayUiState(
    val amount: String = "",
    val isLoading: Boolean = false,
    val message: String? = null
)

class QuickPayViewModel : ViewModel() {

    private val performTapToPay =
        AppContainer.performTapToPayPaymentUseCase

    private val _ui = MutableStateFlow(QuickPayUiState())
    val ui: StateFlow<QuickPayUiState> = _ui

    init {
        // 👂 listen for payment completion
        viewModelScope.launch {
            AppContainer.paymentCompletedTrigger.collect {
                reset()
            }
        }
    }

    fun setAmount(value: String) {
        _ui.update {
            it.copy(
                amount = value,
                message = null
            )
        }
    }

    fun reset() {
        _ui.value = QuickPayUiState()
    }

    fun onPayClicked(
        activity: ComponentActivity,
        paymentLauncher: ActivityResultLauncher<Intent>,
        currency: String = "EUR"
    ) {
        val amount = _ui.value.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _ui.update { it.copy(message = "Enter a valid amount") }
            return
        }

        _ui.update { it.copy(isLoading = true, message = null) }

        viewModelScope.launch {
            try {
                performTapToPay(
                    activity = activity,
                    paymentLauncher = paymentLauncher,
                    amount = amount,
                    currency = currency
                )
                _ui.update {
                    it.copy(
                        isLoading = false,
                        message = "Tap phone to pay..."
                    )
                }
            } catch (e: Exception) {
                _ui.update {
                    it.copy(
                        isLoading = false,
                        message = "Failed: ${e.message}"
                    )
                }
            }
        }
    }
}
