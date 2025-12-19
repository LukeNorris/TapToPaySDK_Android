// domain/usecase/PerformPaymentUseCase.kt
package com.example.taptopaysdk.domain.usecase

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.example.taptopaysdk.domain.model.PaymentMethod
import com.example.taptopaysdk.domain.payment.PaymentExecutor

class PerformPaymentUseCase(
    private val paymentExecutor: PaymentExecutor
) {
    suspend operator fun invoke(
        activity: ComponentActivity,
        paymentLauncher: ActivityResultLauncher<Intent>,
        amount: Double,
        currency: String,
        method: PaymentMethod
    ) {
        paymentExecutor.startPayment(
            activity = activity,
            paymentLauncher = paymentLauncher,
            amount = amount,
            currency = currency,
            method = method
        )
    }
}
