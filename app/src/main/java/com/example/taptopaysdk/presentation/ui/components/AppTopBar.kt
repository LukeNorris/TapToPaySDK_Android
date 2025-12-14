package com.example.taptopaysdk.presentation.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.taptopaysdk.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String?,
    showLogo: Boolean = false,
    onNavBack: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            if (showLogo) {
                Icon(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Logo")
            } else {
                Text(title ?: "", maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        },
        navigationIcon = {
            if (onNavBack != null) {
                IconButton(onClick = onNavBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            }
        }
    )
}
