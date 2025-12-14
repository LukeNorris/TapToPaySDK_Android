// domain/payment/PaymentResultHandler.kt
package com.example.taptopaysdk.domain.payment

import com.example.taptopaysdk.domain.model.Transaction
import com.example.taptopaysdk.domain.usecase.RecordTransactionUseCase
import com.adyen.ipp.api.payment.PaymentResult
import com.example.taptopaysdk.di.AppContainer
import kotlinx.coroutines.flow.MutableSharedFlow

class PaymentResultHandler(
    private val recordTransaction: RecordTransactionUseCase
) {

    suspend fun handleSuccess(paymentResult: PaymentResult): String {
        if (!paymentResult.success) {
            return "Payment failed (not successful)"
        }

        val jsonData = paymentResult.data
        if (jsonData.isBlank()) {
            return "No payment data received"
        }

        return try {
            // Parse key fields from Adyen's Terminal API JSON response
            val pspReference = jsonData
                .substringAfter("\"PSPReference\":\"")
                .substringBefore('"')
                .takeIf { it.length > 5 }
                ?: return "Invalid PSP reference"

            val amountStr = jsonData
                .substringAfter("\"RequestedAmount\":")
                .substringBefore(',')
                .trim()
                .takeIf { it.isNotEmpty() }
                ?: return "Invalid amount"

            val currency = jsonData
                .substringAfter("\"Currency\":\"")
                .substringBefore('"')
                .takeIf { it.isNotEmpty() }
                ?: "EUR"

            val amount = amountStr.toDoubleOrNull()?.div(100.0) ?: return "Invalid amount format"

            // Record via domain use case
            recordTransaction(
                Transaction(
                    id = pspReference,
                    amount = amount,
                    currency = currency,
                    status = "COMPLETED",
                    timestamp = System.currentTimeMillis()
                )
            )
            AppContainer.emitTransactionRefresh()

            "Payment successful! Ref: $pspReference (€${String.format("%.2f", amount)})"
        } catch (e: Exception) {
            // Log the full JSON for debugging (in production, send to analytics)
            android.util.Log.e("PaymentResultHandler", "Parse failed", e)
            "Payment error: ${e.message}"
        }
    }
}