package com.example.taptopaysdk.data.pos

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class NexoPaymentParams(
    val amount: Double,
    val currency: String,
    val paymentType: String = "Normal",   // or "Refund"
    val saleId: String = "AndroidSampleApp",
    val saleReferenceId: String = "DemoSaleReference"
)

/**
 * Result of building a NEXO payment:
 *  - json: the Terminal API request body
 *  - transactionId: the TransactionID we embedded and can expose to domain/UI
 */
data class BuiltNexoPayment(
    val json: String,
    val transactionId: String
)

@RequiresApi(Build.VERSION_CODES.O)
object NexoPaymentBuilder {

    private val DATE_FORMAT: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")

    fun buildPaymentRequest(
        params: NexoPaymentParams,
        poiId: String
    ): BuiltNexoPayment {
        val timeStamp = ZonedDateTime.now().format(DATE_FORMAT)
        val serviceId = UUID.randomUUID().toString().take(10)
        val transactionId = UUID.randomUUID().toString()

        val json = """
        {
          "SaleToPOIRequest": {
            "MessageHeader": {
              "ProtocolVersion": "3.0",
              "MessageClass": "Service",
              "MessageCategory": "Payment",
              "MessageType": "Request",
              "ServiceID": "$serviceId",
              "SaleID": "${params.saleId}",
              "POIID": "$poiId"
            },
            "PaymentRequest": {
              "SaleData": {
                "SaleTransactionID": {
                  "TransactionID": "$transactionId",
                  "TimeStamp": "$timeStamp"
                },
                "SaleReferenceID": "${params.saleReferenceId}",
                "RequestedValidity": "60"
              },
              "PaymentTransaction": {
                "AmountsReq": {
                  "Currency": "${params.currency}",
                  "RequestedAmount": ${params.amount}
                }
              },
              "PaymentData": {
                "PaymentType": "${params.paymentType}"
              }
            }
          }
        }
        """.trimIndent()

        return BuiltNexoPayment(
            json = json,
            transactionId = transactionId
        )
    }
}
