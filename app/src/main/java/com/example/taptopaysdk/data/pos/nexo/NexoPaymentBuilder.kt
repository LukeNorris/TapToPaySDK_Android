package com.example.taptopaysdk.data.pos.nexo

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
                "SaleToAcquirerData": "ewogICAgICJhZGRpdGlvbmFsRGF0YSI6IHsKICAgICAgICAgInNwbGl0LmFwaSI6ICIxIiwKICAgICAgICAgInNwbGl0Lm5yT2ZJdGVtcyI6ICIzIiwKICAgICAgICAgInNwbGl0LnRvdGFsQW1vdW50IjogIjIwMDAiLAogICAgICAgICAic3BsaXQuY3VycmVuY3lDb2RlIjogIkVVUiIsCiAgICAgICAgICJzcGxpdC5pdGVtMS5hbW91bnQiOiAiMTUwMCIsCiAgICAgICAgICJzcGxpdC5pdGVtMS50eXBlIjogIkJhbGFuY2VBY2NvdW50IiwKICAgICAgICAgInNwbGl0Lml0ZW0xLmFjY291bnQiOiAiQkEzMjlCWjIyMzIyQjM1TlJRTFFDODdSSyIsCiAgICAgICAgICJzcGxpdC5pdGVtMS5yZWZlcmVuY2UiOiAicmVmZXJlbmNlX3NwbGl0XzEiLAogICAgICAgICAic3BsaXQuaXRlbTEuZGVzY3JpcHRpb24iOiAiZGVzY3JpcHRpb25fc3BsaXRfMSIsCiAgICAgICAgICJzcGxpdC5pdGVtMi5hbW91bnQiOiAiNTAwIiwKICAgICAgICAgInNwbGl0Lml0ZW0yLnR5cGUiOiAiQ29tbWlzc2lvbiIsCiAgICAgICAgICJzcGxpdC5pdGVtMi5yZWZlcmVuY2UiOiAicmVmZXJlbmNlX2NvbW1pc3Npb24iLAogICAgICAgICAic3BsaXQuaXRlbTIuZGVzY3JpcHRpb24iOiAiZGVzY3JpcHRpb25fY29tbWlzc2lvbiIsCiAgICAgICAgICJzcGxpdC5pdGVtMy50eXBlIjogIlBheW1lbnRGZWUiLAogICAgICAgICAic3BsaXQuaXRlbTMuYWNjb3VudCI6ICJCQTMyQ05IMjIzMjI4NzVMRlNDTkc3RzNIIiwKICAgICAgICAgInNwbGl0Lml0ZW0zLnJlZmVyZW5jZSI6ICJyZWZlcmVuY2VfUGF5bWVudEZlZSIsCiAgICAgICAgICJzcGxpdC5pdGVtMy5kZXNjcmlwdGlvbiI6ICJkZXNjcmlwdGlvbl9QYXltZW50RmVlIgogICAgIH0KfQ==",                
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
