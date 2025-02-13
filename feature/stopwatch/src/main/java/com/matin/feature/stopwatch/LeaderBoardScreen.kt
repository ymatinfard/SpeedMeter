package com.matin.feature.stopwatch

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.matin.core.common.SortOption
import com.matin.core.designsystem.theme.SpeedMeterTheme
import com.matin.core.designsystem.theme.component.CircularImage
import com.matin.core.designsystem.theme.component.SortTab
import com.matin.core.designsystem.theme.component.SpeedMeterTopBar
import com.matin.feature.stopwatch.component.FileExportButton
import com.matin.feature.stopwatch.model.LeaderBoardUiState
import com.matin.feature.stopwatch.model.UiLeaderBoardPlayer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun LeaderBoardScreen(
    viewModel: StopWatchSharedViewModel,
    onStartNewSession: () -> Unit,
    onNavigateToPlayerMetricChart: (Int) -> Unit,
    isDarkTheme: (Boolean) -> Unit
) {
    val state = viewModel.leaderBoardUiState.collectAsStateWithLifecycle()

    LeaderBoardScreenContent(
        state.value,
        viewModel::setSelectedSortOption,
        onStartNewSession,
        viewModel::exportCSVFile,
        onNavigateToPlayerMetricChart,
        isDarkTheme
    )

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LeaderBoardScreenContent(
    state: LeaderBoardUiState,
    onSortOptionSelected: (SortOption) -> Unit = {},
    onStartNewSession: () -> Unit,
    onExportCSV: () -> Unit = {},
    onNavigateToPlayerMetricChart: (Int) -> Unit,
    isDarkTheme: (Boolean) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .width(300.dp)
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(10.dp))
                ThemeMenu(isDarkTheme)
            }
        })
    {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                SpeedMeterTopBar(
                    title = "LeaderBoard",
                    actionUI = { FileExportButton(onExport = onExportCSV) },
                    navigationButton = {
                        IconButton(onClick = {
                            drawerState.apply {
                                scope.launch {
                                    if (isOpen) drawerState.close() else drawerState.open()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "menu"
                            )
                        }
                    })
            },
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
                                modifier = Modifier
                                    .animateItem()
                                    .clickable { onNavigateToPlayerMetricChart(player.id) },
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
                        Text(
                            "Start New Session",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }

}

@Composable
private fun ThemeMenu(
    isDarkTheme: (Boolean) -> Unit,
) {
    val themeOptions = listOf("Light", "Dark", "System")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(themeOptions[0]) }
    Column(
        modifier = Modifier.selectableGroup()
    ) {
        Text("Theme")
        Spacer(modifier = Modifier.height(10.dp))
        themeOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                            isDarkTheme(text == "Dark")
                        },
                        role = Role.RadioButton
                    )
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp)
                )
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
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "Peak Speed: ${String.format("%.2f", player.peakSpeed)} m/s",
                        fontSize = 14.sp,
                        style = if (sortOption == SortOption.EXPLOSIVENESS) MaterialTheme.typography.labelLarge else MaterialTheme.typography.labelMedium,
                        overflow = TextOverflow.Ellipsis
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

@Preview
@Composable
fun LeaderBoardWithEnhancedStylePreview() {
    val samplePlayers = listOf(
        UiLeaderBoardPlayer(id = 0, "Alice", 120f, emptyList(), "https://example.com", 30f),
        UiLeaderBoardPlayer(id = 1, "Bob", 110f, emptyList(), "https://example.com", 30f),
        UiLeaderBoardPlayer(
            id = 2,
            "Charlie",
            130f,
            emptyList(),
            "https://example.com",
            30f
        ),
        UiLeaderBoardPlayer(id = 3, "Diana", 100f, emptyList(), "https://example.com", 30f)
    )
    SpeedMeterTheme {
        LeaderBoardScreenContent(
            state = LeaderBoardUiState(players = samplePlayers),
            {},
            {},
            {},
            {},
            {})
    }
}
