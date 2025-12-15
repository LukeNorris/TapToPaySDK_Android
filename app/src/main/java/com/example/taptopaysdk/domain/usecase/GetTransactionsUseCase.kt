// domain/usecase/GetTransactionsUseCase.kt
package com.example.taptopaysdk.domain.usecase

import com.example.taptopaysdk.domain.model.Transaction
import com.example.taptopaysdk.domain.repository.TransactionRepository

class GetTransactionsUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(): List<Transaction> {
        return repository.getLastTransactions()
    }
}
