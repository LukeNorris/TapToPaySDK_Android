package com.example.taptopaysdk.presentation.navigation

sealed class Screen(val route: String) {
    data object TransactionsTab : Screen("transactions_tab")
    data object QuickPayTab : Screen("quick_pay_tab")
}