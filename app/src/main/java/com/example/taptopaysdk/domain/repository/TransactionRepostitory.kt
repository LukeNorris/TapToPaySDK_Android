package com.example.taptopaysdk.domain.repository

import com.example.taptopaysdk.domain.model.Transaction

interface TransactionRepository {
    suspend fun getLastTransactions(): List<Transaction>
    suspend fun recordTransaction(transaction: Transaction)
    suspend fun markRefunded(transactionId: String)
    suspend fun clear()
}