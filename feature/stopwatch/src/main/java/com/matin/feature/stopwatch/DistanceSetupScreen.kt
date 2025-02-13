package com.matin.feature.stopwatch

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.matin.core.designsystem.theme.SpeedMeterTheme
import com.matin.core.designsystem.theme.component.BackButton
import com.matin.core.designsystem.theme.component.CircularImage
import com.matin.core.designsystem.theme.component.SpeedMeterTopBar
import com.matin.feature.stopwatch.model.CurrentSelectedPlayer
import com.matin.speedmeter.feature.stopwatch.R

@Composable
fun DistanceSetupScreen(
    viewModel: StopWatchSharedViewModel,
    onBack: () -> Unit,
    onNavigateToStopWatch: () -> Unit
) {
    val selectedPlayer by viewModel.currentSelectedPlayer.collectAsStateWithLifecycle()

    DistanceSetupScreenContent(
        selectedPlayer = selectedPlayer,
        onBack = onBack
    ) { distance ->
        viewModel.setCurrentSelectedPlayer(distance = distance)
        onNavigateToStopWatch()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun DistanceSetupScreenContent(
    selectedPlayer: CurrentSelectedPlayer,
    onBack: () -> Unit,
    onNavigateToStopWatch: (distance: Float) -> Unit
) {
    var distance by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            SpeedMeterTopBar(
                title = stringResource(id = R.string.feature_stopwatch_distance_setup),
                navigationButton = {
                    BackButton(onBack)
                })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularImage(imageUrl = selectedPlayer.player?.imageUrl.orEmpty())
                Spacer(Modifier.height(30.dp))
                Text(
                    text = selectedPlayer.player?.fullName.orEmpty(),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = distance,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() || char == '.' } && it.length < 7) {
                            distance = it.trim()
                        }
                    },
                    label = { Text(stringResource(id = R.string.feature_stopwatch_distance_in_meter)) },
                    modifier = Modifier.width(200.dp),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(26.dp))

                val distanceValue = distance.toFloatOrNull()
                Button(
                    onClick = { distanceValue?.let { onNavigateToStopWatch(it) } },
                    enabled = distance.isNotBlank() && distanceValue != null && distanceValue > 0f,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer)
                ) {
                    Text(text = stringResource(id = R.string.feature_stopwatch_start_training_session), fontSize = 18.sp)
                }
            }
        }
    }
}

@Preview
@Composable
fun DistanceSetupScreenPreview() {
    SpeedMeterTheme {
        DistanceSetupScreenContent(CurrentSelectedPlayer(), {}, {})
    }
}
