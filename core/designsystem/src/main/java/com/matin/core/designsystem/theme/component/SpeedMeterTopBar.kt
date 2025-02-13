package com.matin.core.designsystem.theme.component

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SpeedMeterTopBar(
    title: String,
    navigationButton: (@Composable () -> Unit)? = null,
    actionUI: (@Composable () -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (navigationButton != null) {
                navigationButton()
            }
        },
        actions = {
            if (actionUI != null) {
                actionUI()
            }
        }
    )
}