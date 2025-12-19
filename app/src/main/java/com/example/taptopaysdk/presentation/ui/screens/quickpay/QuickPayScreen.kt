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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import com.example.taptopaysdk.domain.model.PaymentMethod

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
            modifier = Modifier
                .width(280.dp)
                .padding(start = 15.dp)

        )

        Spacer(Modifier.height(40.dp))

        PaymentMethodToggle(
            selected = ui.paymentMethod,
            onSelected = vm::setPaymentMethod,
            modifier = Modifier.width(220.dp)
        )
        Spacer(Modifier.height(20.dp))


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


@Composable
fun PaymentMethodToggle(
    selected: PaymentMethod,
    onSelected: (PaymentMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    val pillFraction by animateFloatAsState(
        targetValue = if (selected == PaymentMethod.TAP_TO_PAY) 0f else 1f,
        animationSpec = tween(durationMillis = 220),
        label = "pillOffset"
    )

    BoxWithConstraints(
        modifier = modifier
            .height(32.dp)
            .clip(MaterialTheme.shapes.large)
    ) {
        val containerWidthPx = constraints.maxWidth
        val pillWidthPx = containerWidthPx / 2
        val offsetPx = (pillFraction * pillWidthPx).toInt()

        // 🔵 Sliding pill
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetPx, 0) }
                .width(with(LocalDensity.current) { pillWidthPx.toDp() })
                .fillMaxHeight()
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.primary)
        )

        // 📝 Labels
        Row(modifier = Modifier.fillMaxSize()) {
            ToggleLabel(
                text = "Tap to Pay",
                selected = selected == PaymentMethod.TAP_TO_PAY,
                onClick = { onSelected(PaymentMethod.TAP_TO_PAY) }
            )
            ToggleLabel(
                text = "Card Reader",
                selected = selected == PaymentMethod.CARD_READER,
                onClick = { onSelected(PaymentMethod.CARD_READER) }
            )
        }
    }
}


@Composable
private fun RowScope.ToggleLabel(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = if (selected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}



@Composable
private fun RowScope.SegmentedButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f) // ✅ now valid
            .fillMaxHeight()
            .background(
                if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    Color.Transparent
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = if (selected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
