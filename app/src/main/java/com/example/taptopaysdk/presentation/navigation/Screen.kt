package com.example.taptopaysdk.presentation.navigation

sealed class Screen(val route: String) {
    // Bottom tabs (top-level destinations)
    data object TransactionsTab : Screen("transactions_tab")
    data object QuickPayTab : Screen("quick_pay_tab")

}