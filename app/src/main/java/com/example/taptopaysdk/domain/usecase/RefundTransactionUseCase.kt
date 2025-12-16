package com.example.taptopaysdk.domain.usecase

import android.util.Log
import com.example.taptopaysdk.domain.repository.ReversalRepository

class RefundTransactionUseCase(
    private val reversalRepository: ReversalRepository
) {
    suspend operator fun invoke(
        transactionId: String,
        timestampMillis: Long
    ): Result<Unit> {

        Log.d("RefundUseCase", "Starting refund for $transactionId")

        // ✅ Delegate SDK + JSON building to data layer
        return reversalRepository.reverseTransaction(
            transactionId = transactionId,
            timestamp = timestampMillis
        )
    }
}
