package com.example.taptopaysdk.presentation.ui.screens.settings

// ui/screens/InstallationIdScreen.kt
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.adyen.ipp.api.InPersonPayments

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstallationIdScreen(navController: NavController) {

    var installationId by remember { mutableStateOf("Loading...") }

    LaunchedEffect(Unit) {
        installationId = InPersonPayments.getInstallationId().getOrElse { "Unavailable" }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Installation ID") },
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
        ) {
            Text("ID: $installationId")
        }
    }
}


