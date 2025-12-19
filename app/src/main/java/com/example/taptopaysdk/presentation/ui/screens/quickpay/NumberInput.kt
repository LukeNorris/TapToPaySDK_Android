package com.example.taptopaysdk.presentation.ui.screens.quickpay

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumberInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .width(150.dp)
            .padding(horizontal = 40.dp)
    ) {
        Text(
            text = "€",
            fontSize = 40.sp,
            modifier = Modifier.padding(end = 4.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(1.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    var filtered = newValue.filter { it.isDigit() || it == '.' }
                    if (filtered.count { it == '.' } > 1) return@BasicTextField
                    if (filtered.startsWith(".")) filtered = "0$filtered"

                    val dotIndex = filtered.indexOf('.')
                    if (dotIndex != -1 && filtered.length - dotIndex > 3) return@BasicTextField

                    val numericValue = filtered.toDoubleOrNull()
                    if (numericValue != null && numericValue > 1000) return@BasicTextField

                    onValueChange(filtered)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 55.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (value.isEmpty()) {
                Text(
                    text = "0.00",
                    fontSize = 60.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
