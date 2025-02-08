package com.matin.feature.stopwatch

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.matin.core.common.TimeFormatter
import com.matin.core.designsystem.theme.SpeedMeterTheme
import com.matin.feature.stopwatch.model.StopwatchState
import com.matin.feature.stopwatch.model.TimeLap
import kotlinx.coroutines.delay

@Composable
fun StopWatchScreen(viewModel: StopWatchSharedViewModel) {
    val state = viewModel.stopWatchUiState.collectAsStateWithLifecycle()

    StopWatchScreenContent(
        state.value,
        onUpdateTime = viewModel::updateTime,
        toggleTimer = viewModel::toggleTimer,
        addLap = viewModel::addLap,
        reset = viewModel::resetTimer
    )
}

@Composable
fun StopWatchScreenContent(
    state: StopwatchState,
    onUpdateTime: () -> Unit = {},
    toggleTimer: () -> Unit = {},
    addLap: () -> Unit = {},
    reset: () -> Unit = {}
) {

    LaunchedEffect(state.isRunning) {
        while (state.isRunning) {
            delay(100L)
            onUpdateTime()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { StopwatchTopBar() }
    ) { padding ->
        StopwatchContent(
            state = state,
            onStartStop = toggleTimer,
            onLap = addLap,
            onReset = reset,
            modifier = Modifier.padding(padding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StopwatchTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Stopwatch",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    )
}

@Composable
private fun StopwatchContent(
    state: StopwatchState,
    onStartStop: () -> Unit,
    onLap: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Timer(timeInMillis = state.timeInMillis)
        Spacer(modifier = Modifier.height(16.dp))
        LapsList(modifier = Modifier.weight(1f), laps = state.laps)
        Spacer(modifier = Modifier.height(16.dp))
        Controls(
            isRunning = state.isRunning,
            onStartStop = onStartStop,
            onLap = onLap,
            onReset = onReset
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
    Text(
        text = "Lap Times",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(laps.reversed()) { lap ->
            LapItem(lap = lap)
        }
    }
}

@Composable
private fun LapItem(lap: TimeLap) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "#${lap.lapCount}",
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
    onReset: () -> Unit
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
            onClick = onReset,
            text = "Reset"
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
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
@Preview(showBackground = true)
fun StopwatchScreenPreview() {
    SpeedMeterTheme {
        StopWatchScreenContent(state = StopwatchState())
    }
}

