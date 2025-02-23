package com.matin.feature.stopwatch.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.matin.feature.stopwatch.StopwatchSharedViewModel
import com.matin.feature.stopwatch.screen.LeaderBoardScreen

const val LEADER_BOARD_ROUTE = "leader_board_route"

fun NavGraphBuilder.leaderBoardScreenRoute(viewModel: StopwatchSharedViewModel, onStartNewSession: () -> Unit, onNavigateToPlayerMetricChart: (String) -> Unit, isDarkTheme: (Boolean) -> Unit) {
    composable(LEADER_BOARD_ROUTE) {
        LeaderBoardScreen(viewModel = viewModel, onStartNewSession, onNavigateToPlayerMetricChart, isDarkTheme)
    }
}