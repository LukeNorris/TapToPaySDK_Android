package com.example.taptopaysdk.domain.repository

interface ReversalRepository {
    suspend fun reverseTransaction(
        transactionId: String,
        timestamp: Long
    ): Result<Unit>
}
