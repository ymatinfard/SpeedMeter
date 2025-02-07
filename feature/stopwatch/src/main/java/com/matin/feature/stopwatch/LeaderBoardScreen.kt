package com.matin.feature.stopwatch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.matin.core.designsystem.theme.SpeedMeterTheme
import com.matin.feature.stopwatch.model.UiPlayer
import com.matin.speedmeter.feature.stopwatch.R

@Composable
fun LeaderBoardScreen(viewModel: StopWatchSharedViewModel) {
    val players = viewModel.players.collectAsStateWithLifecycle()
    val selectedSortOption = viewModel.sortOption.collectAsStateWithLifecycle()

    LeaderBoardScreenContent(players.value, selectedSortOption.value, viewModel::onSortOptionSelected)
}

@Composable
fun LeaderBoardScreenContent(
    players: List<UiPlayer> = emptyList(),
    selectedSortOption: SortOption = SortOption.EXPLOSIVENESS,
    onSortOptionSelected: (SortOption) -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Leaderboard",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SortButton(
                text = "Explosiveness",
                isSelected = selectedSortOption == SortOption.EXPLOSIVENESS,
                onClick = { onSortOptionSelected(SortOption.EXPLOSIVENESS) }
            )
            SortButton(
                text = "Endurance",
                isSelected = selectedSortOption == SortOption.ENDURANCE,
                onClick = { onSortOptionSelected(SortOption.ENDURANCE) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(players.size) { index ->
                PlayerCard(player = players[index], sortOption = selectedSortOption)
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
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
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
fun PlayerCard(player: UiPlayer, sortOption: SortOption) {
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
        UiPlayer("Alice", 120, 10, R.drawable.feature_stopwatch_ic_person),
        UiPlayer("Bob", 110, 15, R.drawable.feature_stopwatch_ic_person),
        UiPlayer("Charlie", 130, 8, R.drawable.feature_stopwatch_ic_person),
        UiPlayer("Diana", 100, 20, R.drawable.feature_stopwatch_ic_person)
    )
    SpeedMeterTheme {
        LeaderBoardScreenContent(players = samplePlayers)
    }
}
