// presentation/ui/screens/transactions/TransactionsScreen.kt
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

@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel = viewModel()
) {
    val transactions by viewModel.transactions.collectAsState()

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
                    TransactionItem(tx)
                    Divider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionItem(transaction: Transaction) {
    ListItem(
        headlineContent = {
            Text(
                text = transaction.id,
                fontWeight = FontWeight.Medium
            )
        },
        supportingContent = {
            Text(
                "${transaction.amount} ${transaction.currency} • ${transaction.status}"
            )
        },
        trailingContent = {
            Text(
                text = formatTimestamp(transaction.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}

private fun formatTimestamp(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < 60_000 -> "just now"
        diff < 3_600_000 -> "${diff / 60_000}m ago"
        diff < 86_400_000 -> "${diff / 3_600_000}h ago"
        else -> "${diff / 86_400_000}d ago"
    }
}
