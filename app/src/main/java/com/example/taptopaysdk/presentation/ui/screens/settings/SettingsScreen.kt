package com.example.taptopaysdk.presentation.ui.screens.settings

// ui/screens/SettingsScreen.kt
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TouchApp
import com.example.taptopaysdk.presentation.navigation.Routes
import androidx.compose.ui.graphics.vector.ImageVector


@Composable
fun SettingsScreen(navController: NavController) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp))
    {

        SettingItem(
            title = "Session Info",
            icon = Icons.Default.Info,
            onClick = { navController.navigate(Routes.SessionInfo) }
        )

        Spacer(Modifier.height(8.dp))

        SettingItem(
            title = "Installation ID",
            icon = Icons.Default.TouchApp,
            onClick = { navController.navigate(Routes.InstallationId) }
        )
    }
}

@Composable
fun SettingItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Icon(icon, title)
            Spacer(Modifier.width(16.dp))
            Text(title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
