// presentation/ui/screens/transactions/TransactionsViewModel.kt

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

    private val getTransactionsUseCase = AppContainer.getTransactionsUseCase

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    init {
        loadTransactions()

        // THIS LINE WAS MISSING — THIS IS THE BUG
        viewModelScope.launch {
            AppContainer.transactionRefreshTrigger.collect {
                loadTransactions()
            }
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            try {
                val list = getTransactionsUseCase()
                _transactions.value = list
            } catch (e: Exception) {
                _transactions.value = emptyList()
            }
        }
    }

    fun refresh() = loadTransactions()
}