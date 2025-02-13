package com.matin.feature.stopwatch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.matin.core.common.Result
import com.matin.core.designsystem.theme.SpeedMeterTheme
import com.matin.core.designsystem.theme.component.BackButton
import com.matin.core.designsystem.theme.component.CircularImage
import com.matin.core.designsystem.theme.component.LoadingWheel
import com.matin.core.designsystem.theme.component.SpeedMeterTopBar
import com.matin.feature.stopwatch.model.UiPlayerSelection

@Composable
fun PlayerSelectionScreen(
    viewModel: StopWatchSharedViewModel,
    onBack: () -> Unit,
    onNavigateToDistanceSetup: () -> Unit
) {
    val uiState = viewModel.playerListUiState.collectAsStateWithLifecycle()
    PlayerSelectionScreenContent(
        uiState.value,
        retry = viewModel::retryPlayerList,
        onBack,
        onItemClick = {
            viewModel.setSelectedPlayer(it)
            onNavigateToDistanceSetup()
        })
}

@Composable
fun PlayerSelectionScreenContent(
    uiState: Result<List<UiPlayerSelection>>,
    retry: () -> Unit = {},
    onBack: () -> Unit,
    onItemClick: (UiPlayerSelection) -> Unit = {}
) {
    Scaffold(
        topBar = {
            SpeedMeterTopBar(
                title = "Player Selection",
                navigationButton = { BackButton(action = onBack) })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (uiState) {
                is Result.Success -> {
                    PlayerSelectionList(uiState.data, onItemClick)
                }

                Result.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LoadingWheel()
                    }
                }

                is Result.Error -> {
                    when {
                        uiState.data == null -> {
                            Retry(retry)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Retry(retry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Something went wrong",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = { retry() },
            colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.errorContainer)
        ) {
            Text(text = "Retry")
        }
    }
}


@Composable
fun PlayerSelectionList(
    players: List<UiPlayerSelection>,
    onItemClick: (UiPlayerSelection) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(players) {
                PlayerItem(it, onItemClick)
            }
        }
    }
}

@Composable
fun PlayerItem(player: UiPlayerSelection, onItemClick: (UiPlayerSelection) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onItemClick(player) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            CircularImage(player.imageUrl)
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = player.fullName,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun PlayerItemPreview() {
    SpeedMeterTheme {
        PlayerSelectionList(
            listOf(UiPlayerSelection("John Doe", "https://example.com/image.jpg")),
            {})
    }
}