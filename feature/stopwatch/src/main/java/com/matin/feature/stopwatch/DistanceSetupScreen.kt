package com.matin.feature.stopwatch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.matin.core.designsystem.theme.SpeedMeterTheme
import com.matin.feature.stopwatch.model.CurrentSelectedPlayer

@Composable
fun DistanceSetupScreen(viewModel: StopWatchSharedViewModel, onNavigateToStopWatch: () -> Unit) {
    val selectedPlayer = viewModel.currentSelectedPlayer.collectAsStateWithLifecycle()

    DistanceSetupScreenContent(selectedPlayer.value) { distance ->
        viewModel.setCurrentSelectedPlayer(distance = distance.toDouble())

        onNavigateToStopWatch()
    }
}

@Composable
private fun DistanceSetupScreenContent(
    selectedPlayer: CurrentSelectedPlayer,
    onNavigateToStopWatch: (distance: String) -> Unit
) {
    var distance by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularImage(imageUrl = selectedPlayer.player?.imageUrl.toString())
            Spacer(Modifier.height(30.dp))
            Text(
                text = "Setup Distance for ${selectedPlayer.player?.fullName}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = distance,
                onValueChange = { distance = it },
                label = { Text("Enter distance in meters") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(26.dp))
            Button(
                onClick = { onNavigateToStopWatch(distance) },
                enabled = distance.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer)
            ) {
                Text(text = "Start Training Session", fontSize = 18.sp)
            }
        }
    }
}

@Preview
@Composable
fun DistanceSetupScreenPreview() {
    SpeedMeterTheme {
        DistanceSetupScreenContent(CurrentSelectedPlayer(), {})
    }
}