package com.example.taptopaysdk.presentation.ui.screens.settings

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.adyen.ipp.cardreader.api.ui.DeviceManagementActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardReaderScreen(navController: NavController) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Card Readers") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding -> // Use the padding provided by Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // This prevents content from going under the TopBar
                .padding(24.dp),       // Standardize with your other screens
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Pair, manage, and update your physical card readers via the Adyen Device Manager.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))

            // The Action Button
            Button(
                onClick = {
                    (context as? Activity)?.let { activity ->
                        DeviceManagementActivity.start(activity)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // Match the height of your Pay button
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Open Device Manager")
            }
        }
    }
}