package com.example.taptopaysdk.data.pos

import android.util.Log
import com.adyen.ipp.api.InPersonPayments
import com.adyen.ipp.api.payment.TransactionReversalRequest

object TapToPayReversalExecutor {

    suspend fun performReversal(
        nexoJson: String
    ): Result<Unit> {

        Log.d("ReversalExecutor", "Sending reversal JSON: $nexoJson")

        val reversalRequestResult =
            TransactionReversalRequest.createReversal(nexoJson)

        return reversalRequestResult.fold(
            onSuccess = { request ->
                InPersonPayments
                    .performReversal(request)
                    .map { paymentResult ->
                        Log.d(
                            "ReversalExecutor",
                            "Reversal completed. success=${paymentResult.success}"
                        )
                        Unit // 👈 THIS is the fix
                    }
            },
            onFailure = { error ->
                Log.e(
                    "ReversalExecutor",
                    "Failed to create reversal request",
                    error
                )
                Result.failure(error)
            }
        )
    }
}
