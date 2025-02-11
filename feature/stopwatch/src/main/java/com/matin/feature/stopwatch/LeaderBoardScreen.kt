package com.matin.feature.stopwatch

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.matin.core.common.SortOption
import com.matin.core.designsystem.theme.SpeedMeterTheme
import com.matin.core.designsystem.theme.component.SortTab
import com.matin.core.designsystem.theme.component.SpeedMeterTopBar
import com.matin.feature.stopwatch.component.FileExportButton
import com.matin.feature.stopwatch.model.LeaderBoardUiState
import com.matin.feature.stopwatch.model.UiLeaderBoardPlayer

@Composable
fun LeaderBoardScreen(viewModel: StopWatchSharedViewModel, onStartNewSession: () -> Unit) {
    val state = viewModel.leaderBoardUiState.collectAsStateWithLifecycle()

    LeaderBoardScreenContent(
        state.value,
        viewModel::setSelectedSortOption,
        onStartNewSession,
        viewModel::exportCSVFile
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LeaderBoardScreenContent(
    state: LeaderBoardUiState,
    onSortOptionSelected: (SortOption) -> Unit = {},
    onStartNewSession: () -> Unit,
    onExportCSV: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            SpeedMeterTopBar(
                title = "LeaderBoard",
                actionUI = { FileExportButton(onExport = onExportCSV) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            if (state.players.isEmpty()) {
                LeaderboardEmptyState(modifier = Modifier.weight(1f))
            } else {
                Spacer(Modifier.height(16.dp))
                SortTab(
                    modifier = Modifier.fillMaxWidth(),
                    tabsList = SortOption.entries,
                    initialSelectedOption = state.sortOption,
                    onClick = onSortOptionSelected
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.players, key = { player -> player.id }) { player ->
                        PlayerCard(
                            modifier = Modifier.animateItem(),
                            player = player,
                            sortOption = state.sortOption
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier.width(250.dp),
                    shape = RoundedCornerShape(12.dp),
                    onClick = { onStartNewSession() }) {
                    Text("Start New Session", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
fun PlayerCard(modifier: Modifier, player: UiLeaderBoardPlayer, sortOption: SortOption) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularImage(imageUrl = player.imageUrl)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = player.fullName,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "Peak Speed: ${String.format("%.2f", player.peakSpeed)} m/s",
                        fontSize = 14.sp,
                        style = if (sortOption == SortOption.EXPLOSIVENESS) MaterialTheme.typography.labelLarge else MaterialTheme.typography.labelMedium,
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Laps: ${player.laps.size}",
                        fontSize = 14.sp,
                        style = if (sortOption == SortOption.ENDURANCE) MaterialTheme.typography.labelLarge else MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
    }
}

@Composable
fun LeaderboardEmptyState(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            imageVector = ImageVector.vectorResource(com.matin.speedmeter.core.common.R.drawable.ic_speed_meter),
            contentDescription = "Empty State",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier.size(70.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Leaderboard is currently empty",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderBoardWithEnhancedStylePreview() {
    val samplePlayers = listOf(
        UiLeaderBoardPlayer(id = 0, "Alice", 120f, emptyList(), "https://example.com"),
        UiLeaderBoardPlayer(id = 1, "Bob", 110f, emptyList(), "https://example.com"),
        UiLeaderBoardPlayer(id = 2, "Charlie", 130f, emptyList(), "https://example.com"),
        UiLeaderBoardPlayer(id = 3, "Diana", 100f, emptyList(), "https://example.com")
    )
    SpeedMeterTheme {
        LeaderBoardScreenContent(state = LeaderBoardUiState(players = samplePlayers), {}, {})
    }
}
