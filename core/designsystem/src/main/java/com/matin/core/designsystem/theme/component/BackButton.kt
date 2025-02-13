package com.matin.core.designsystem.theme.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun BackButton(action: () -> Unit) {
    IconButton(onClick = action) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "back"
        )
    }
}