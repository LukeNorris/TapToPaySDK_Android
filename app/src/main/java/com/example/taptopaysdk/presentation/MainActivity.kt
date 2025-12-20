// MainActivity.kt
package com.example.taptopaysdk.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.adyen.ipp.api.InPersonPayments
import com.example.taptopaysdk.presentation.navigation.AppNavHost
import com.example.taptopaysdk.presentation.navigation.BottomBar
import com.example.taptopaysdk.presentation.navigation.Screen
import com.example.taptopaysdk.presentation.ui.components.AppTopBar
import com.example.taptopaysdk.presentation.theme.AppTheme
import androidx.lifecycle.lifecycleScope
import com.example.taptopaysdk.di.AppContainer
import com.example.taptopaysdk.presentation.navigation.Routes
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    lateinit var paymentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.TRANSPARENT // Background remains transparent
            )
        )
        paymentLauncher = InPersonPayments.registerForPaymentResult(this) { result ->
            result.fold(
                onSuccess = { paymentResult ->
                  Log.d("MainActivity", "Payment callback received")

                    // Delegate to domain handler (non-blocking)
                    lifecycleScope.launch {
                        val message = AppContainer.paymentResultHandler.handleSuccess(paymentResult)
                        Log.d("MainActivity", message)  // Or show Snackbar later
                    }
                    AppContainer.emitPaymentCompleted()
                },
                onFailure = { error ->
                    lifecycleScope.launch {
                        AppContainer.paymentResultHandler
                            .handleFailure(error)
                    }
                }
            )
        }
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val currentDestination = navController.currentBackStackEntryAsState().value?.destination
                val currentRoute = currentDestination?.route

                val title = when (currentRoute) {
                    Screen.QuickPayTab.route -> "Quick Pay"
                    Screen.TransactionsTab.route -> "Transactions"
                    Routes.Settings -> "Settings"
                    else -> "TapToPay Demo"
                }

                val showBottomBar = currentRoute == Screen.QuickPayTab.route ||
                        currentRoute == Screen.TransactionsTab.route ||
                        currentRoute == Routes.Settings

                Scaffold(
                    topBar = {
                        AppTopBar(title = title, showLogo = false, onNavBack = null)
                    },
                    bottomBar = {
                        if (showBottomBar) BottomBar(navController)
                    }
                ) { padding ->
                    AppNavHost(
                        navController = navController,
                        paymentLauncher = paymentLauncher,
                        modifier = Modifier.padding(
                            top = padding.calculateTopPadding(),
                            bottom = 0.dp // <--- THIS IGNORES THE BOTTOM BAR GAP
                        )
                    )
                }
            }
        }
    }
}