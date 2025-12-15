package com.example.taptopaysdk.domain.payment

import android.util.Base64
import android.util.Log
import com.adyen.ipp.api.payment.PaymentResult
import com.example.taptopaysdk.di.AppContainer
import com.example.taptopaysdk.domain.model.Transaction
import com.example.taptopaysdk.domain.model.TransactionStatus
import com.example.taptopaysdk.domain.usecase.RecordTransactionUseCase
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class PaymentResultHandler(
    private val recordTransaction: RecordTransactionUseCase
) {

    suspend fun handleSuccess(paymentResult: PaymentResult): String {
        Log.wtf("PaymentResultHandler", "🚀 handleSuccess() ENTERED")
        if (!paymentResult.success) {
            return "Payment failed (not successful)"
        }

        // 1️⃣ Decode Base64
        val decodedJson = try {
            String(
                Base64.decode(paymentResult.data, Base64.NO_WRAP),
                StandardCharsets.UTF_8
            )
        } catch (e: Exception) {
            Log.e("PaymentResultHandler", "Base64 decode failed", e)
            return "Invalid payment data encoding"
        }

        Log.d("PaymentResultHandler", "Decoded payment JSON = $decodedJson")

        return try {
            val root = JSONObject(decodedJson)

            val paymentResponse =
                root.getJSONObject("SaleToPOIResponse")
                    .getJSONObject("PaymentResponse")

            // 2️⃣ Transaction ID + timestamp
            val poiTx =
                paymentResponse
                    .getJSONObject("POIData")
                    .getJSONObject("POITransactionID")

            val transactionId =
                poiTx.getString("TransactionID")

            val timestampMillis =
                OffsetDateTime
                    .parse(
                        poiTx.getString("TimeStamp"),
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    )
                    .toInstant()
                    .toEpochMilli()

            // 3️⃣ Status
            val result =
                paymentResponse
                    .getJSONObject("Response")
                    .getString("Result")

            val status = mapResultToStatus(result)

            // 4️⃣ Amount & currency (authoritative)
            val amountsResp =
                paymentResponse
                    .getJSONObject("PaymentResult")
                    .optJSONObject("AmountsResp")

            val amount =
                amountsResp?.optDouble("AuthorizedAmount") ?: 0.0

            val currency =
                amountsResp?.optString("Currency") ?: "EUR"

            // 5️⃣ Persist
            recordTransaction(
                Transaction(
                    id = transactionId,
                    amount = amount,
                    currency = currency,
                    status = status,
                    timestamp = timestampMillis
                )
            )

            AppContainer.emitTransactionRefresh()

            "Payment $result – Ref: $transactionId (€${"%.2f".format(amount)})"
        } catch (e: Exception) {
            Log.e("PaymentResultHandler", "Parse failed", e)
            "Payment error: ${e.message}"
        }
    }

    suspend fun handleFailure(error: Throwable): String {
        recordTransaction(
            Transaction(
                id = "FAILED_${System.currentTimeMillis()}",
                amount = 0.0,
                currency = "EUR",
                status = TransactionStatus.FAILED,
                timestamp = System.currentTimeMillis()
            )
        )

        AppContainer.emitTransactionRefresh()

        return "Payment failed: ${error.message}"
    }

    private fun mapResultToStatus(result: String): TransactionStatus =
        when (result.uppercase()) {
            "SUCCESS" -> TransactionStatus.COMPLETED
            "FAILURE", "FAILED", "ERROR" -> TransactionStatus.FAILED
            else -> TransactionStatus.FAILED
        }
}
