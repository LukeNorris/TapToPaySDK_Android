package com.example.taptopaysdk.data.pos

import android.util.Log
import com.adyen.ipp.api.InPersonPayments
import com.example.taptopaysdk.domain.repository.ReversalRepository

class ReversalRepositoryImpl : ReversalRepository {

    override suspend fun reverseTransaction(
        transactionId: String,
        timestamp: Long
    ): Result<Unit> {

        // 1️⃣ Get installation ID from SDK (data layer ✅)
        val poiId = InPersonPayments
            .getInstallationId()
            .getOrElse { error ->
                Log.e("ReversalRepo", "Failed to get installationId", error)
                return Result.failure(error)
            }

        // 2️⃣ Build reversal JSON
        val json =
            NexoReversalBuilder.buildReversalRequest(
                params = NexoReversalParams(
                    originalTransactionId = transactionId,
                    originalTimestamp = timestamp
                ),
                poiId = poiId
            )

        Log.d("ReversalRepo", "Built reversal JSON:\n$json")

        // 3️⃣ Call SDK reversal
        return TapToPayReversalExecutor.performReversal(json)
    }
}
