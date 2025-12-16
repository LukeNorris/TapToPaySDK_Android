package com.example.taptopaysdk.presentation.ui.screens.quickpay

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickPayScreen(
    paymentLauncher: ActivityResultLauncher<Intent>
) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val vm: QuickPayViewModel = viewModel()
    val ui by vm.ui.collectAsState()

    val focusManager = LocalFocusManager.current

    // 👇 clear focus when amount resets
    LaunchedEffect(ui.amount) {
        if (ui.amount.isEmpty()) {
            focusManager.clearFocus()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(64.dp))
        Text("Quick Pay", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))

        NumberInput(
            value = ui.amount,
            onValueChange = vm::setAmount,
            modifier = Modifier.width(280.dp)
        )

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = {
                activity?.let {
                    vm.onPayClicked(it, paymentLauncher)
                }
            },
            enabled = !ui.isLoading && ui.amount.isNotBlank(),
            modifier = Modifier
                .width(280.dp)
                .height(60.dp)
        ) {
            Text(
                if (ui.isLoading) "Starting..." else "TAP TO PAY",
                style = MaterialTheme.typography.titleMedium
            )
        }

        ui.message?.let {
            Spacer(Modifier.height(24.dp))
            Text(
                it,
                color = if (it.contains("Failed", true))
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary
            )
        }
    }
}