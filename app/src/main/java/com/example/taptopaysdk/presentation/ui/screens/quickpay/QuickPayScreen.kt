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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.sp
import com.example.taptopaysdk.domain.model.PaymentMethod
import com.example.taptopaysdk.presentation.ui.components.AppTopBar

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
        Spacer(Modifier.height(100.dp))

        Text(
            "Enter Amount",
            fontSize = 30.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(10.dp))

        NumberInput(
            value = ui.amount,
            onValueChange = vm::setAmount,
            modifier = Modifier.width(280.dp)
        )

        Spacer(Modifier.height(40.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = ui.paymentMethod == PaymentMethod.TAP_TO_PAY,
                onClick = { vm.setPaymentMethod(PaymentMethod.TAP_TO_PAY) },
                label = { Text("Tap to Pay") }
            )

            FilterChip(
                selected = ui.paymentMethod == PaymentMethod.CARD_READER,
                onClick = { vm.setPaymentMethod(PaymentMethod.CARD_READER) },
                label = { Text("Card Reader") }
            )
        }

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
                if (ui.isLoading) "Starting..." else "PAY",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 25.sp
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
