// presentation/navigation/AppNavHost.kt
package com.example.taptopaysdk.presentation.navigation

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taptopaysdk.presentation.ui.screens.quickpay.QuickPayScreen
import com.example.taptopaysdk.presentation.ui.screens.transactions.TransactionsScreen
import com.example.taptopaysdk.presentation.ui.screens.transactions.TransactionsViewModel
import com.example.taptopaysdk.presentation.ui.screens.settings.SettingsScreen
import com.example.taptopaysdk.presentation.ui.screens.settings.SessionInfoScreen
import com.example.taptopaysdk.presentation.ui.screens.settings.InstallationIdScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    paymentLauncher: ActivityResultLauncher<Intent>,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.QuickPayTab.route,
        modifier = modifier
    ) {
        composable(Screen.QuickPayTab.route) {
            QuickPayScreen(
                navController = navController,
                paymentLauncher = paymentLauncher
            )
        }

        composable(Screen.TransactionsTab.route) {
            val viewModel: TransactionsViewModel = viewModel()  // ← THIS SAVES YOUR LIFE
            TransactionsScreen(viewModel = viewModel)
        }

        composable(Routes.Settings) {
            SettingsScreen(navController)
        }

        composable(Routes.SessionInfo) {
            SessionInfoScreen(
                navController,
                paymentLauncher = paymentLauncher
            )
        }

        composable(Routes.InstallationId) {
            InstallationIdScreen(navController)
        }
    }
}