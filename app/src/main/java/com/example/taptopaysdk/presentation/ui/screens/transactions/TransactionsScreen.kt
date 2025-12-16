package com.example.taptopaysdk.presentation.ui.screens.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taptopaysdk.domain.model.Transaction
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel = viewModel()
) {
    val transactions by viewModel.transactions.collectAsState()
    val refundingIds by viewModel.refundingIds.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        if (transactions.isEmpty()) {
            Text(
                text = "No transactions yet",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(transactions, key = { it.id }) { tx ->
                    TransactionItem(
                        transaction = tx,
                        isRefunding = refundingIds.contains(tx.id),
                        onRefund = { viewModel.refundTransaction(tx.id) }
                    )
                    Divider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionItem(
    transaction: Transaction,
    isRefunding: Boolean,
    onRefund: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = "${transaction.amount} ${transaction.currency}",
                fontWeight = FontWeight.Medium
            )
        },
        supportingContent = {
            Text(formatTimestamp(transaction.timestamp))
        },
        trailingContent = {
            when {
                transaction.isRefunded -> {
                    Text(
                        text = "Refunded",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                isRefunding -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                }

                else -> {
                    OutlinedButton(
                        onClick = onRefund
                    ) {
                        Text("Refund")
                    }
                }
            }
        }
    )
}

/**
 * Converts epoch millis → "16 Dec 2025, 09:45"
 */
private fun formatTimestamp(timestamp: Long): String {
    val formatter =
        DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")

    return Instant
        .ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}
