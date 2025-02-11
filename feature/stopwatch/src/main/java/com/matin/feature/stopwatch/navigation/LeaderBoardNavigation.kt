package com.matin.feature.stopwatch.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.matin.feature.stopwatch.LeaderBoardScreen
import com.matin.feature.stopwatch.StopWatchSharedViewModel

const val LEADER_BOARD_ROUTE = "leader_board_route"

fun NavGraphBuilder.leaderBoardScreenRoute(viewModel: StopWatchSharedViewModel, onStartNewSession: () -> Unit, onNavigateToPlayerMetricChart: (Int) -> Unit) {
    composable(LEADER_BOARD_ROUTE) {
        LeaderBoardScreen(viewModel = viewModel, onStartNewSession, onNavigateToPlayerMetricChart)
    }
}