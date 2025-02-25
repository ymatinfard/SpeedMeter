package com.matin.feature.stopwatch.screen

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
import androidx.compose.material3.DrawerState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.matin.core.common.SortOption
import com.matin.core.designsystem.theme.SpeedMeterTheme
import com.matin.core.designsystem.theme.component.CircularImage
import com.matin.core.designsystem.theme.component.ErrorState
import com.matin.core.designsystem.theme.component.FullScreenLoadingIndicator
import com.matin.core.designsystem.theme.component.SortTab
import com.matin.core.designsystem.theme.component.SpeedMeterTopBar
import com.matin.feature.stopwatch.StopwatchSharedViewModel
import com.matin.feature.stopwatch.component.FileExportButton
import com.matin.feature.stopwatch.model.LeaderBoarUiData
import com.matin.feature.stopwatch.model.LeaderBoardUiState
import com.matin.feature.stopwatch.model.UiLeaderBoardPlayer
import com.matin.feature.stopwatch.ui.LeaderBoardPreviewParameterProvider
import com.matin.speedmeter.feature.stopwatch.R

@Composable
fun LeaderBoardScreen(
    viewModel: StopwatchSharedViewModel,
    onStartNewSession: () -> Unit,
    onNavigateToPlayerMetricChart: (String) -> Unit,
) {
    val state by viewModel.leaderboard.collectAsStateWithLifecycle()
    LeaderBoardScreenStateHandler(
        state = state,
        onSortOptionSelected = viewModel::updateSortOption,
        onStartNewSession = onStartNewSession,
        onExportCSV = viewModel::exportToCsv,
        onNavigateToPlayerMetricChart = onNavigateToPlayerMetricChart,
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LeaderBoardScreenStateHandler(
    state: LeaderBoardUiState,
    onSortOptionSelected: (SortOption) -> Unit = {},
    onStartNewSession: () -> Unit = {},
    onExportCSV: () -> Unit = {},
    onNavigateToPlayerMetricChart: (String) -> Unit = {},
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    SpeedMeterNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent()
        }
    ) {
        when (state) {
            is LeaderBoardUiState.Success ->
                LeaderBoardScreenContent(
                    state.data,
                    onExportCSV,
                    onSortOptionSelected,
                    onNavigateToPlayerMetricChart,
                    onStartNewSession,
                    toggleDrawer = {
//                        scope.launch {
//                            drawerState.apply {
//                                if (isOpen) drawerState.close() else drawerState.open()
//                            }
//                        }
                    }
                )

            is LeaderBoardUiState.Loading -> FullScreenLoadingIndicator()
            is LeaderBoardUiState.Error -> ErrorState()
        }
    }
}

@Composable
private fun LeaderBoardScreenContent(
    data: LeaderBoarUiData,
    onExportCSV: () -> Unit,
    onSortOptionSelected: (SortOption) -> Unit,
    onNavigateToPlayerMetricChart: (String) -> Unit,
    onStartNewSession: () -> Unit,
    toggleDrawer: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            SpeedMeterTopBar(
                title = stringResource(R.string.feature_stopwatch_leaderboard),
                actionUI = { FileExportButton(onExport = onExportCSV) },
                navigationButton = {
                    IconButton(onClick = toggleDrawer) {
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
            if (data.players.isEmpty()) {
                EmptyLeaderBoardState(modifier = Modifier.weight(1f))
            } else {
                Spacer(Modifier.height(16.dp))
                SortTab(
                    modifier = Modifier.fillMaxWidth(),
                    tabsList = SortOption.entries,
                    initialSelectedOption = data.sortOption,
                    onClick = onSortOptionSelected
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(data.players, key = { player -> player.sessionId }) { player ->
                        PlayerCard(
                            modifier = Modifier
                                .animateItem()
                                .clickable { onNavigateToPlayerMetricChart(player.sessionId) },
                            player = player,
                            sortOption = data.sortOption
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
                        stringResource(R.string.feature_stopwatch_start_new_session),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
fun SpeedMeterNavigationDrawer(
    drawerState: DrawerState,
    drawerContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { drawerContent() }) {
        content()
    }
}

@Composable
private fun NavigationDrawerContent(isDarkTheme: (Boolean) -> Unit = {}) {
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
        ThemeSelectionMenu(isDarkTheme)
    }
}

enum class ThemeOption { LIGHT, DARK }

@Composable
private fun ThemeSelectionMenu(
    isDarkTheme: (Boolean) -> Unit,
) {
    val themeOptions = listOf(ThemeOption.LIGHT, ThemeOption.DARK)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(ThemeOption.LIGHT) }
    Column(
        modifier = Modifier.selectableGroup()
    ) {
        Text("Theme")
        Spacer(modifier = Modifier.height(10.dp))
        themeOptions.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .selectable(
                        selected = (option == selectedOption),
                        onClick = {
                            onOptionSelected(option)
                            isDarkTheme(option == ThemeOption.DARK)
                        },
                        role = Role.RadioButton
                    )
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = null
                )
                Text(
                    text = option.name,
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
                        text = stringResource(
                            R.string.feature_stopwatch_peak_speed_m_s,
                            String.format("%.2f", player.peakSpeed)
                        ),
                        fontSize = 14.sp,
                        style = if (sortOption == SortOption.EXPLOSIVENESS) MaterialTheme.typography.labelLarge else MaterialTheme.typography.labelMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.feature_stopwatch_laps, player.laps.size),
                        fontSize = 14.sp,
                        style = if (sortOption == SortOption.ENDURANCE) MaterialTheme.typography.labelLarge else MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyLeaderBoardState(modifier: Modifier) {
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
            text = stringResource(R.string.feature_stopwatch_leaderboard_is_currently_empty),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview
@Composable
fun LeaderBoardWithEnhancedStylePreview(
    @PreviewParameter(LeaderBoardPreviewParameterProvider::class)
    leaderBoardUiState: LeaderBoardUiState
) {
    SpeedMeterTheme {
        LeaderBoardScreenStateHandler(
            state = leaderBoardUiState,
            {},
            {},
            {},
            {})
    }
}
