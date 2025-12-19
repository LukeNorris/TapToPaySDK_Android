package com.example.taptopaysdk.presentation.ui.screens.settings

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.adyen.ipp.cardreader.api.ui.DeviceManagementActivity

@Composable
fun CardReaderScreen(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Card Readers",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Pair, manage, and update your card readers.",
            style = MaterialTheme.typography.bodyMedium
        )

        Button(
            onClick = {
                (context as? Activity)?.let { activity ->
                    DeviceManagementActivity.start(activity)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open Device Manager")
        }

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
