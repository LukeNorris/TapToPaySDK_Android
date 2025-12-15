// data/transaction/TransactionRepositoryImpl.kt
package com.example.taptopaysdk.data.transaction

import android.content.Context
import android.content.SharedPreferences
import com.example.taptopaysdk.domain.model.Transaction
import com.example.taptopaysdk.domain.model.TransactionStatus
import com.example.taptopaysdk.domain.repository.TransactionRepository
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log


class TransactionRepositoryImpl(
    context: Context
) : TransactionRepository {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("transaction_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_HISTORY = "last_5_transactions"
        private const val MAX_SIZE = 5
    }

    override suspend fun getLastTransactions(): List<Transaction> {
        val raw = prefs.getString(KEY_HISTORY, "[]") ?: "[]"

        android.util.Log.d(
            "TransactionRepo",
            "Raw stored JSON = $raw"
        )

        val array = JSONArray(raw)

        val transactions = List(array.length()) { index ->
            val o = array.getJSONObject(index)
            Transaction(
                id = o.getString("id"),
                timestamp = o.getLong("timestamp"),
                amount = o.optDouble("amount"),
                currency = o.getString("currency"),
                status = runCatching {
                    TransactionStatus.valueOf(o.getString("status"))
                }.getOrElse {
                    TransactionStatus.FAILED
                },
                isRefunded = o.optBoolean("refunded", false)
            )
        }

        android.util.Log.d(
            "TransactionRepo",
            "Parsed transactions (${transactions.size}): $transactions"
        )

        return transactions
    }


    override suspend fun recordTransaction(transaction: Transaction) {
        Log.d("TransactionRepo", "recordTransaction(): $transaction")

        val current = getLastTransactions().toMutableList()

        current.removeAll { it.id == transaction.id }
        current.add(0, transaction)

        if (current.size > MAX_SIZE) {
            current.removeLast()
        }

        save(current)
    }

    override suspend fun markRefunded(transactionId: String) {
        val updated = getLastTransactions().map {
            if (it.id == transactionId) it.copy(isRefunded = true) else it
        }
        save(updated)
    }

    override suspend fun clear() {
        prefs.edit().remove(KEY_HISTORY).apply()
    }

    private fun save(list: List<Transaction>) {
        val array = JSONArray().apply {
            list.forEach {
                put(
                    JSONObject().apply {
                        put("id", it.id)
                        put("timestamp", it.timestamp)
                        put("amount", it.amount)
                        put("status", it.status.name)
                        put("currency", it.currency)
                        put("refunded", it.isRefunded)
                    }
                )
            }
        }

        prefs.edit().putString(KEY_HISTORY, array.toString()).apply()
    }
}
