
// domain/usecase/PerformTapToPayPaymentUseCase.kt
package com.example.taptopaysdk.domain.usecase

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.example.taptopaysdk.data.pos.TapToPayExecutor

class PerformTapToPayPaymentUseCase(
    private val tapToPayExecutor: TapToPayExecutor
) {
    suspend operator fun invoke(
        activity: ComponentActivity,
        paymentLauncher: ActivityResultLauncher<Intent>,
        amount: Double,
        currency: String = "EUR"
    ) {
        tapToPayExecutor.startPayment(
            activity = activity,
            paymentLauncher = paymentLauncher,
            amount = amount,
            currency = currency
        )
    }
}