package com.example.taptopaysdk.data.pos.nexo

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class NexoReversalParams(
    val originalTransactionId: String,
    val originalTimestamp: Long,
    val saleId: String = "AndroidSampleApp",
    val reversalReason: String = "MerchantCancel"
)

@RequiresApi(Build.VERSION_CODES.O)
object NexoReversalBuilder {

    private val DATE_FORMAT =
        DateTimeFormatter.ISO_OFFSET_DATE_TIME

    fun buildReversalRequest(
        params: NexoReversalParams,
        poiId: String
    ): String {

        val serviceId = UUID.randomUUID().toString().take(10)

        val isoTimestamp =
            OffsetDateTime
                .ofInstant(
                    java.time.Instant.ofEpochMilli(params.originalTimestamp),
                    java.time.ZoneOffset.UTC
                )
                .format(DATE_FORMAT)

        return """
        {
          "SaleToPOIRequest": {
            "MessageHeader": {
              "ProtocolVersion": "3.0",
              "MessageClass": "Service",
              "MessageCategory": "Reversal",
              "MessageType": "Request",
              "SaleID": "${params.saleId}",
              "ServiceID": "$serviceId",
              "POIID": "$poiId"
            },
            "ReversalRequest": {
              "OriginalPOITransaction": {
                "POITransactionID": {
                  "TransactionID": "${params.originalTransactionId}",
                  "TimeStamp": "$isoTimestamp"
                }
              },
              "ReversalReason": "${params.reversalReason}"
            }
          }
        }
        """.trimIndent()
    }
}
