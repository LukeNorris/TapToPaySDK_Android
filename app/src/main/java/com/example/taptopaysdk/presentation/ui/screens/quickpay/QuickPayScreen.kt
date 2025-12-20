package com.example.taptopaysdk.presentation.ui.screens.quickpay

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taptopaysdk.domain.model.PaymentMethod
import java.text.NumberFormat
import java.util.Locale

data class CurrencyOption(val label: String, val code: String, val locale: Locale)

val currencyOptions = listOf(
    CurrencyOption("EUR", "EUR", Locale.GERMANY),
    CurrencyOption("USD", "USD", Locale.US),
    CurrencyOption("GBP", "GBP", Locale.UK)
)

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickPayScreen(
    paymentLauncher: ActivityResultLauncher<Intent>
) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val vm: QuickPayViewModel = viewModel()
    val ui by vm.ui.collectAsState()
    val focusManager = LocalFocusManager.current

    val density = LocalDensity.current
    val isKeyboardOpen = WindowInsets.ime.getBottom(density) > 0
    val navBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(ui.cents) {
        if (ui.cents == 0) focusManager.clearFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 24.dp)
    ) {
        // --- MINIMAL CURRENCY SELECTOR (DROPDOWN) ---
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                Surface(
                    modifier = Modifier.menuAnchor(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    onClick = { expanded = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val currentOption = currencyOptions.find { it.code == ui.currencyCode }
                        Text(
                            text = currentOption?.label ?: "Select Currency",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp).padding(start = 4.dp)
                        )
                    }
                }

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    currencyOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label) },
                            onClick = {
                                vm.setCurrency(option.code, option.locale)
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        }

        // --- CENTERED AREA ---
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-50).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(
                visible = ui.cents == 0,
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    "Enter Amount",
                    fontSize = 22.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            NumberInput(
                cents = ui.cents,
                onCentsChange = vm::setCents,
                modifier = Modifier.width(IntrinsicSize.Min),
                locale = ui.locale
            )
        }

        // --- BOTTOM ACTIONS ---
        AnimatedVisibility(
            visible = ui.cents > 0,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = if (isKeyboardOpen) 16.dp else navBarHeight + 90.dp
                    )
            ) {




                Button(
                    onClick = {
                        focusManager.clearFocus()
                        activity?.let { vm.onPayClicked(it, paymentLauncher) }
                    },
                    enabled = !ui.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (ui.isLoading) "Starting..."
                        else "Charge ${formatCentsToCurrency(ui.cents, ui.locale)}",
                        fontSize = 20.sp
                    )
                }
                Spacer(Modifier.height(25.dp))
                PaymentMethodToggle(
                    selected = ui.paymentMethod,
                    onSelected = vm::setPaymentMethod
                )
            }
        }
    }
}*/

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

    val density = LocalDensity.current
    val isKeyboardOpen = WindowInsets.ime.getBottom(density) > 0
    val navBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(ui.cents) {
        if (ui.cents == 0) focusManager.clearFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 24.dp)
    ) {
        // --- CURRENCY SELECTOR (Top) ---
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        ) {
            // ... (Dropdown logic remains the same)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                Surface(
                    modifier = Modifier.menuAnchor(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    onClick = { expanded = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val currentOption = currencyOptions.find { it.code == ui.currencyCode }
                        Text(
                            text = currentOption?.label ?: "Select Currency",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp).padding(start = 4.dp)
                        )
                    }
                }
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    currencyOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label) },
                            onClick = {
                                vm.setCurrency(option.code, option.locale)
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        }

        // --- CENTERED AREA (Amount + Charge Button) ---
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-40).dp), // Adjusted offset to account for button below
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(
                visible = ui.cents == 0,
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    "Enter Amount",
                    fontSize = 22.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            NumberInput(
                cents = ui.cents,
                onCentsChange = vm::setCents,
                modifier = Modifier.width(IntrinsicSize.Min),
                locale = ui.locale
            )

            Spacer(Modifier.height(32.dp)) // Gap between number and button

            Button(
                onClick = {
                    focusManager.clearFocus()
                    activity?.let { vm.onPayClicked(it, paymentLauncher) }
                },
                enabled = ui.cents > 0 && !ui.isLoading,
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Slightly narrower looks better in the center
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
            ) {
                Text(
                    text = if (ui.isLoading) "Starting..."
                    else "Charge ${formatCentsToCurrency(ui.cents, ui.locale)}",
                    fontSize = 20.sp
                )
            }
        }

        // --- BOTTOM AREA (Toggle Only) ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(
                    bottom = if (isKeyboardOpen) 16.dp else navBarHeight + 35.dp
                )
        ) {
            PaymentMethodToggle(
                selected = ui.paymentMethod,
                onSelected = vm::setPaymentMethod
            )
        }
    }
}

@Composable
fun PaymentMethodToggle(
    selected: PaymentMethod,
    onSelected: (PaymentMethod) -> Unit
) {
    val animatedOffset by animateDpAsState(
        targetValue = if (selected == PaymentMethod.TAP_TO_PAY) 0.dp else 125.dp,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "pillOffset"
    )

    Box(
        modifier = Modifier
            .size(width = 250.dp, height = 36.dp)
            .graphicsLayer {
                shape = RoundedCornerShape(18.dp)
                clip = true
            }
            .background(Color.LightGray.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier
                .offset(x = animatedOffset)
                .size(width = 125.dp, height = 36.dp)
                .padding(2.dp)
                .graphicsLayer {
                    shape = RoundedCornerShape(16.dp)
                    clip = true
                }
                .background(Color(0xFFE0E0E0))
        )

        Row(modifier = Modifier.fillMaxSize()) {
            ToggleLabel("Tap to Pay", selected == PaymentMethod.TAP_TO_PAY) { onSelected(PaymentMethod.TAP_TO_PAY) }
            ToggleLabel("Card Reader", selected == PaymentMethod.CARD_READER) { onSelected(PaymentMethod.CARD_READER) }
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
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = animateColorAsState(
                targetValue = if (selected) Color.Black else Color.Gray,
                animationSpec = tween(160)
            ).value
        )
    }
}

private fun formatCentsToCurrency(cents: Int, locale: Locale): String {
    val formatter = NumberFormat.getCurrencyInstance(locale)
    val amount = cents / 100.0
    return formatter.format(amount)
}