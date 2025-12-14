// data/repository/TransactionRepositoryImpl.kt
package com.example.taptopaysdk.data.transaction

import com.example.taptopaysdk.domain.model.Transaction
import com.example.taptopaysdk.domain.repository.TransactionRepository

class TransactionRepositoryImpl : TransactionRepository {

    private val _transactions = mutableListOf<Transaction>()

    override suspend fun getTransactions(): List<Transaction> =
        _transactions.toList()

    override suspend fun addTransaction(transaction: Transaction) {
        _transactions.add(0, transaction) // newest first
        if (_transactions.size > 100) _transactions.removeLast()
        println("TRANSACTION SAVED! Count = ${_transactions.size} | ID = ${transaction.id}")
        android.util.Log.e("REPO", "TRANSACTION SAVED! Count = ${_transactions.size} | PSP: ${transaction.id}")
    }
}