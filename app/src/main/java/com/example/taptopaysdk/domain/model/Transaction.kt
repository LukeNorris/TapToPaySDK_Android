// domain/model/Transaction.kt
package com.example.taptopaysdk.domain.model

data class Transaction(
    val id: String,
    val amount: Double,
    val currency: String = "EUR",
    val status: TransactionStatus,
    val timestamp: Long = System.currentTimeMillis(),
    val isRefunded: Boolean = false
)