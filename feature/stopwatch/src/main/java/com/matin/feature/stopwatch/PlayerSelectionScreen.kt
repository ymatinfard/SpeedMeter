package com.matin.feature.stopwatch

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.matin.core.common.Result
import com.matin.core.designsystem.theme.SpeedMeterTheme
import com.matin.feature.stopwatch.model.UiPlayerSelection

@Composable
fun PlayerSelectionScreen(
    viewModel: StopWatchSharedViewModel,
    onNavigateToDistanceSetup: () -> Unit
) {
    val uiState = viewModel.playerListState.collectAsStateWithLifecycle()
    PlayerSelectionScreenContent(uiState.value) {
        viewModel.setSelectedPlayer(it)
        onNavigateToDistanceSetup()
    }
}

@Composable
fun PlayerSelectionScreenContent(
    uiState: Result<List<UiPlayerSelection>>,
    onItemClick: (UiPlayerSelection) -> Unit = {}
) {
    when (uiState) {
        is Result.Success -> {
            Log.d("playerSelectionScreenContent", uiState.data.toString())
            PlayerSelectionList(uiState.data, onItemClick)
        }

        Result.Loading -> {
            Text("Loading")
        }

        is Result.Error -> {
            Text(text = uiState.throwable.message.toString())
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSelectionList(
    players: List<UiPlayerSelection>,
    onItemClick: (UiPlayerSelection) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Player Selection",
                        style = MaterialTheme.typography.headlineSmall
                    )
                })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
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
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun CircularImage(imageUrl: String) {
    AsyncImage(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape),
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl).crossfade(true).build(),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
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