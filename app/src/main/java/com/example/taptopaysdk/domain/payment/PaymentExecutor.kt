package com.example.taptopaysdk.domain.payment

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.example.taptopaysdk.domain.model.PaymentMethod

interface PaymentExecutor {
    suspend fun startPayment(
        activity: ComponentActivity,
        paymentLauncher: ActivityResultLauncher<Intent>,
        amount: Double,
        currency: String,
        method: PaymentMethod
    )
}