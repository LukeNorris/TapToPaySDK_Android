package com.example.taptopaysdk.presentation.ui.screens.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taptopaysdk.di.AppContainer
import com.example.taptopaysdk.domain.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionsViewModel : ViewModel() {

    private val getTransactionsUseCase =
        AppContainer.getTransactionsUseCase

    private val transactionRepository =
        AppContainer.transactionRepository

    private val _transactions =
        MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> =
        _transactions.asStateFlow()

    // 👇 Track transactions currently being refunded
    private val _refundingIds =
        MutableStateFlow<Set<String>>(emptySet())
    val refundingIds: StateFlow<Set<String>> =
        _refundingIds.asStateFlow()

    init {
        loadTransactions()

        viewModelScope.launch {
            AppContainer.transactionRefreshTrigger.collect {
                loadTransactions()
            }
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            _transactions.value = runCatching {
                getTransactionsUseCase()
            }.getOrElse {
                emptyList()
            }
        }
    }

    fun refundTransaction(transactionId: String) {
        viewModelScope.launch {

            val tx = _transactions.value
                .firstOrNull { it.id == transactionId }
                ?: return@launch

            if (tx.isRefunded) return@launch

            // ⏳ mark as loading
            _refundingIds.value =
                _refundingIds.value + transactionId

            AppContainer
                .refundTransactionUseCase(
                    tx.id,
                    tx.timestamp
                )
                .onSuccess {
                    transactionRepository.markRefunded(tx.id)
                    AppContainer.emitTransactionRefresh()
                }
                .onFailure {
                    android.util.Log.e(
                        "TransactionsViewModel",
                        "Refund failed for ${tx.id}",
                        it
                    )
                }

            // ✅ remove loading state (success or failure)
            _refundingIds.value =
                _refundingIds.value - transactionId
        }
    }
}
