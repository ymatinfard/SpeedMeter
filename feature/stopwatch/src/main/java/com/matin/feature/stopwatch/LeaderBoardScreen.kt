package com.matin.feature.stopwatch

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.matin.core.designsystem.theme.SpeedMeterTheme
import com.matin.feature.stopwatch.model.LeaderBoardUiState
import com.matin.feature.stopwatch.model.UiLeaderBoardPlayer
import com.matin.speedmeter.feature.stopwatch.R

@Composable
fun LeaderBoardScreen(viewModel: StopWatchSharedViewModel, onStartNewSession: () -> Unit) {
    val state = viewModel.leaderBoardUiState.collectAsStateWithLifecycle()

    LeaderBoardScreenContent(state.value, viewModel::setSelectedSortOption, onStartNewSession)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderBoardScreenContent(
    state: LeaderBoardUiState,
    onSortOptionSelected: (SortOption) -> Unit = {},
    onStartNewSession: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Leader Board",
                        style = MaterialTheme.typography.headlineSmall
                    )
                })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Spacer(Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SortButton(
                    text = "Explosiveness",
                    isSelected = state.sortOption == SortOption.EXPLOSIVENESS,
                    onClick = { onSortOptionSelected(SortOption.EXPLOSIVENESS) }
                )
                SortButton(
                    text = "Endurance",
                    isSelected = state.sortOption == SortOption.ENDURANCE,
                    onClick = { onSortOptionSelected(SortOption.ENDURANCE) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.players) { player ->
                    PlayerCard(player = player, sortOption = state.sortOption)
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
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
fun SortButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer,
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer,
            style = if (isSelected) MaterialTheme.typography.labelLarge else MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun PlayerCard(player: UiLeaderBoardPlayer, sortOption: SortOption) {
    Card(
        modifier = Modifier
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
            Image(
                painter = painterResource(id = player.pictureRes),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh, shape = CircleShape)
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = player.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "Peak Speed: ${player.peakSpeed} km/h",
                        fontSize = 14.sp,
                        style = if (sortOption == SortOption.EXPLOSIVENESS) MaterialTheme.typography.labelLarge else MaterialTheme.typography.labelMedium,
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Laps: ${player.laps}",
                        fontSize = 14.sp,
                        style = if (sortOption == SortOption.ENDURANCE) MaterialTheme.typography.labelLarge else MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
    }
}

enum class SortOption { EXPLOSIVENESS, ENDURANCE }

@Composable
@Preview(showBackground = true)
fun LeaderBoardWithEnhancedStylePreview() {
    val samplePlayers = listOf(
        UiLeaderBoardPlayer("Alice", 120, 10, R.drawable.feature_stopwatch_ic_person),
        UiLeaderBoardPlayer("Bob", 110, 15, R.drawable.feature_stopwatch_ic_person),
        UiLeaderBoardPlayer("Charlie", 130, 8, R.drawable.feature_stopwatch_ic_person),
        UiLeaderBoardPlayer("Diana", 100, 20, R.drawable.feature_stopwatch_ic_person)
    )
    SpeedMeterTheme {
        LeaderBoardScreenContent(state = LeaderBoardUiState(players = samplePlayers), {}, {})
    }
}
