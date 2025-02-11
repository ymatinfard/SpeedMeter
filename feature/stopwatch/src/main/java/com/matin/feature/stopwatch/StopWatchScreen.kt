package com.matin.feature.stopwatch

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.matin.core.common.TimeFormatter
import com.matin.core.designsystem.theme.SpeedMeterTheme
import com.matin.core.designsystem.theme.component.PlayerInfo
import com.matin.core.designsystem.theme.component.SpeedMeterTopBar
import com.matin.feature.stopwatch.model.CurrentSelectedPlayer
import com.matin.feature.stopwatch.model.StopwatchState
import com.matin.feature.stopwatch.model.TimeLap
import kotlinx.coroutines.delay

@Composable
fun StopWatchScreen(viewModel: StopWatchSharedViewModel, onBack: () -> Unit) {
    val stopWatchState = viewModel.stopWatchUiState.collectAsStateWithLifecycle()
    val playerState = viewModel.currentSelectedPlayer.collectAsStateWithLifecycle()

    StopWatchScreenContent(
        stopWatchState.value,
        currentPlayer = playerState.value,
        toggleTimer = viewModel::toggleTimer,
        addLap = viewModel::addLap,
        save = viewModel::saveSessionAndRest,
        onBack = onBack,
    )
}

@Composable
fun StopWatchScreenContent(
    state: StopwatchState,
    currentPlayer: CurrentSelectedPlayer,
    toggleTimer: () -> Unit = {},
    addLap: () -> Unit = {},
    save: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    BackHandler {
        save()
        onBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SpeedMeterTopBar("StopWatch", {
                save()
                onBack()
            })
        }
    ) { padding ->
        StopwatchContent(
            state = state,
            currentPlayer = currentPlayer,
            onStartStop = toggleTimer,
            onLap = addLap,
            onSave = save,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
private fun StopwatchContent(
    state: StopwatchState,
    currentPlayer: CurrentSelectedPlayer,
    onStartStop: () -> Unit,
    onLap: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlayerInfo(
            currentPlayer.player?.fullName ?: "",
            currentPlayer.distance,
            currentPlayer.player?.imageUrl ?: ""
        )
        Spacer(modifier = Modifier.height(16.dp))
        Timer(timeInMillis = state.timeInMillis)
        Spacer(modifier = Modifier.height(16.dp))
        LapsList(modifier = Modifier.weight(1f), laps = state.laps)
        Spacer(modifier = Modifier.height(16.dp))
        Controls(
            isRunning = state.isRunning,
            onStartStop = onStartStop,
            onLap = onLap,
            onSaveSession = onSave
        )
    }
}

@Composable
private fun Timer(timeInMillis: Long) {
    Text(
        text = TimeFormatter.formatTime(timeInMillis),
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
private fun LapsList(modifier: Modifier, laps: List<TimeLap>) {
    val listState = rememberLazyListState()
    LaunchedEffect(laps) {
        delay(300)
        listState.animateScrollToItem(0)
    }
    Text(
        text = "Lap Times",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    LazyColumn(
        modifier = modifier,
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val peakSpeed = laps.minOfOrNull { it.lapTime } ?: -1
        val lowestSpeed = laps.maxOfOrNull { it.lapTime } ?: -1
        items(laps.reversed(), key = { it.id }) { lap ->
            val cardColor = pickCardColor(lap.lapTime, peakSpeed, lowestSpeed)
            LapItem(modifier = Modifier.animateItem(), lap, cardColor)
        }
    }
}

@Composable
private fun pickCardColor(
    lap: Long,
    peakSpeed: Long,
    lowestSpeed: Long
): Color {
    return when (lap) {
        peakSpeed -> Color.Green.copy(alpha = .3f)
        lowestSpeed -> Color.Red.copy(alpha = .3f)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
}

@Composable
private fun LapItem(modifier: Modifier, lap: TimeLap, cardColor: Color) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(cardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "#${lap.id}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = TimeFormatter.formatTime(lap.lapTime),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = TimeFormatter.formatTime(lap.totalTime),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun Controls(
    isRunning: Boolean,
    onStartStop: () -> Unit,
    onLap: () -> Unit,
    onSaveSession: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StopwatchButton(
            onClick = onStartStop,
            text = if (isRunning) "Stop" else "Start"
        )
        StopwatchButton(
            onClick = onLap,
            text = "Lap",
            enabled = isRunning
        )
        StopwatchButton(
            onClick = onSaveSession,
            text = "Save and Reset",
            enabled = !isRunning
        )
    }
}

@Composable
private fun StopwatchButton(
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
@Preview(showBackground = true)
fun StopwatchScreenPreview() {
    SpeedMeterTheme {
        StopWatchScreenContent(state = StopwatchState(), CurrentSelectedPlayer())
    }
}

