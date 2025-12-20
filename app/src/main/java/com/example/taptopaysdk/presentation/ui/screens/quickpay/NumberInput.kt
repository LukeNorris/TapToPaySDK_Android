package com.example.taptopaysdk.presentation.ui.screens.quickpay

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PlatformImeOptions
import java.text.NumberFormat
import java.util.Locale


class AmountVisualTransformation(val locale: Locale) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val original = text.text

        // 1. Convert the string of digits to a currency format
        val centsAsLong = original.toLongOrNull() ?: 0L
        val amount = centsAsLong / 100.0

        // 2. Use NumberFormat to get the locale-specific string (e.g., 1.250,50)
        val formatter = NumberFormat.getCurrencyInstance(locale)
        // Remove the currency symbol from the transformation because you have a separate Text("€")
        val formatted = formatter.format(amount)
            .replace(formatter.currency?.symbol ?: "", "")
            .trim()

        // 3. Simple offset mapping: always keep cursor at the end for PIN-style entry
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = formatted.length
            override fun transformedToOriginal(offset: Int): Int = original.length
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

@Composable
fun NumberInput(
    cents: Int,
    onCentsChange: (Int) -> Unit,
    locale: Locale = Locale.GERMANY, // Default to a European locale
    modifier: Modifier = Modifier,
    maxCents: Int = 999999,
) {
    val digits = remember(cents) { if (cents == 0) "" else cents.toString() }

    // Get the currency symbol from the locale (e.g., €, $, £)
    val currencySymbol = remember(locale) {
        NumberFormat.getCurrencyInstance(locale).currency?.symbol ?: "€"
    }

    BasicTextField(
        value = digits,
        onValueChange = { newDigits ->
            val filtered = newDigits.filter { it.isDigit() }
            if (filtered.isEmpty()) {
                onCentsChange(0)
            } else {
                val newCents = filtered.toLongOrNull()?.coerceAtMost(maxCents.toLong()) ?: 0
                onCentsChange(newCents.toInt())
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
            platformImeOptions = PlatformImeOptions("nm")
        ).copy(autoCorrectEnabled = false),
        singleLine = true,
        // Pass the locale here!
        visualTransformation = remember(locale) { AmountVisualTransformation(locale) },
        textStyle = LocalTextStyle.current.copy(
            fontSize = 55.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start
        ),
        modifier = modifier,
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(
                    text = currencySymbol, // Dynamic Symbol!
                    fontSize = 40.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Box(contentAlignment = Alignment.CenterStart) {
                    if (cents == 0) {
                        // Format the "0.00" placeholder for the locale too
                        val placeholder = if (locale == Locale.US) "0.00" else "0,00"
                        Text(
                            text = placeholder,
                            fontSize = 55.sp,
                            color = Color.Gray.copy(alpha = 0.5f)
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}