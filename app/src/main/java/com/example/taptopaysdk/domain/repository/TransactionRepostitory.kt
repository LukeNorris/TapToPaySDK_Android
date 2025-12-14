package com.example.taptopaysdk.domain.repository
import com.example.taptopaysdk.domain.model.Transaction

interface TransactionRepository {
    suspend fun getTransactions(): List<Transaction>
    suspend fun addTransaction(transaction: Transaction)
}
