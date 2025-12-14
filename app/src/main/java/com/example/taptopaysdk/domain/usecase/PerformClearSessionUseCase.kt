// domain/usecase/PerformTapToPayPaymentUseCase.kt
package com.example.taptopaysdk.domain.usecase

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.example.taptopaysdk.data.pos.ClearSessionExecutor

class PerformClearSessionUseCase(
    private val clearSessionExecutor: ClearSessionExecutor
) {
    suspend operator fun invoke(
        activity: ComponentActivity,
        paymentLauncher: ActivityResultLauncher<Intent>
    ) {
        clearSessionExecutor.clearSession(activity, paymentLauncher)
    }
}
