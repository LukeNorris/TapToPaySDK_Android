package com.example.taptopaysdk.presentation.ui.screens.settings

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taptopaysdk.di.AppContainer
import com.example.taptopaysdk.presentation.ui.screens.quickpay.QuickPayViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionInfoScreen(
    navController: NavController,
    paymentLauncher: ActivityResultLauncher<Intent>
) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    val vm: SessionInfoViewModel = viewModel {
        SessionInfoViewModel(
            repo = AppContainer.sessionRepository,
            clearSessionUseCase = AppContainer.performClearSessionUseCase
        )
    }

    val info by vm.sessionInfo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Session Info") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            val session = info

            if (session == null) {
                Text("No active session", style = MaterialTheme.typography.bodyLarge)
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {

                    // GENERAL
                    SectionCard(title = "General") {
                        InfoRow("Installation ID", session.installationId)
                        InfoRow("Merchant Account", session.merchantAccount)
                        InfoRow("Bundle ID", session.bundleId)
                        InfoRow("Client Key", session.clientKey)
                        InfoRow("Mode", session.mode)
                        InfoRow("Version", session.version.toString())
                    }

                    Spacer(Modifier.height(12.dp))

                    // EXPIRY
                    val formattedDate = formatTimestamp(session.expiresAt)
                    val relative = relativeTimeFromNow(session.expiresAt)
                    SectionCard(title = "Session Expiry") {
                        InfoRow("Expires At", formattedDate)
                        InfoRow("Time Remaining", relative)
                    }

                    Spacer(Modifier.height(12.dp))

                    // DEVICE
                    SectionCard(title = "Device") {
                        InfoRow("Name", session.device.name)
                        InfoRow("Model", session.device.model)
                        InfoRow("System", session.device.systemName)
                        InfoRow("System Version", session.device.systemVersion)
                        InfoRow("User Agent", session.userAgent)
                    }

                    Spacer(Modifier.height(12.dp))

                    // LIBRARY
                    SectionCard(title = "Library") {
                        InfoRow("Name", session.library.name)
                        InfoRow("Version", session.library.version)
                        InfoRow("Build Type", session.library.buildType)
                    }

                    Spacer(Modifier.height(12.dp))

                    // PUBLIC KEY
                    SectionCard(title = "Public Key") {
                        InfoRow("kty", session.publicKey.kty)
                        InfoRow("crv", session.publicKey.crv)
                        InfoRow("x", session.publicKey.x)
                        InfoRow("y", session.publicKey.y)
                    }

                    Spacer(Modifier.height(12.dp))

                    // SECURITY
                    SectionCard(title = "Security") {
                        InfoRow(
                            "Setup Token Public Key Hash",
                            session.setupTokenPublicKeyHash
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // CLEAR SESSION BUTTON
            Button(
                onClick = { activity?.let { vm.clearSession(it, paymentLauncher) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Clear Session", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    if (value.isBlank()) return

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(6.dp))
    }
}

private fun formatTimestamp(epochSeconds: Long): String {
    if (epochSeconds == 0L) return "N/A"
    val date = Date(epochSeconds * 1000)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(date)
}

private fun relativeTimeFromNow(epochSeconds: Long): String {
    if (epochSeconds == 0L) return "Unknown"
    val nowMillis = System.currentTimeMillis()
    val targetMillis = epochSeconds * 1000
    val diffMillis = targetMillis - nowMillis

    if (diffMillis <= 0) return "Expired"

    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
    val days = TimeUnit.MILLISECONDS.toDays(diffMillis)

    return when {
        days > 0  -> "in $days day(s)"
        hours > 0 -> "in $hours hour(s)"
        else      -> "in $minutes minute(s)"
    }
}
