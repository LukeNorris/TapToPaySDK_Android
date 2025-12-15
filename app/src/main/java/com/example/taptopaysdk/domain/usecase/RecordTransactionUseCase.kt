// domain/usecase/RecordTransactionUseCase.kt
package com.example.taptopaysdk.domain.usecase

import com.example.taptopaysdk.domain.model.Transaction
import com.example.taptopaysdk.domain.repository.TransactionRepository

class RecordTransactionUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.recordTransaction(transaction)
    }
}
