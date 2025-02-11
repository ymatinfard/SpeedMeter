package com.matin.feature.stopwatch.component

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.matin.speedmeter.feature.stopwatch.R

class PermissionStateHolder {
    var permissionRequested by mutableStateOf(false)
    var hasPermission by mutableStateOf(false)
}

@Composable
fun FileExportButton(
    onExport: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = stringResource(R.string.feature_stopwatch_export_file)
) {
    val permissionState = remember { PermissionStateHolder() }
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionState.hasPermission = isGranted
        permissionState.permissionRequested = false

        when {
            isGranted -> onExport()
            else -> showPermissionDeniedMessage(context)
        }
    }

    LaunchedEffect(Unit) {
        permissionState.hasPermission = checkNotificationPermission(context)
    }

    IconButton(
        onClick = { handleExportClick(context, permissionState, permissionLauncher, onExport) },
        modifier = modifier
    ) {
        androidx.compose.material3.Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_export_file),
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}

private fun showPermissionDeniedMessage(context: Context) {
    Toast.makeText(
        context,
        context.getString(R.string.feature_stopwatch_permission_denied_message),
        Toast.LENGTH_SHORT
    ).show()
}

private fun checkNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else true
}

private fun handleExportClick(
    context: Context,
    permissionState: PermissionStateHolder,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    onExport: () -> Unit
) {
    when {
        permissionState.hasPermission || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU -> {
            onExport()
        }
        !permissionState.permissionRequested -> {
            permissionState.permissionRequested = true
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}