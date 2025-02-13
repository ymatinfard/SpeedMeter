package com.matin.speedmeter.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.matin.feature.stopwatch.StopWatchSharedViewModel
import com.matin.feature.stopwatch.navigation.LEADER_BOARD_ROUTE
import com.matin.feature.stopwatch.navigation.distanceSetupScreen
import com.matin.feature.stopwatch.navigation.leaderBoardScreenRoute
import com.matin.feature.stopwatch.navigation.navigateToDistanceSetup
import com.matin.feature.stopwatch.navigation.navigateToPlayerMetricChart
import com.matin.feature.stopwatch.navigation.navigateToPlayerSelectionScreen
import com.matin.feature.stopwatch.navigation.navigateToStopWatchScreen
import com.matin.feature.stopwatch.navigation.playerMetricChartScreen
import com.matin.feature.stopwatch.navigation.playerSelectionScreen
import com.matin.feature.stopwatch.navigation.stopWatchScreen

@Composable
fun SpeedMeterNavHost(isDarkTheme: (Boolean) -> Unit) {
    val navController = rememberNavController()
    val viewModel: StopWatchSharedViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = LEADER_BOARD_ROUTE) {
        leaderBoardScreenRoute(
            viewModel,
            { navController.navigateToPlayerSelectionScreen() },
            { id -> navController.navigateToPlayerMetricChart(id) },
            isDarkTheme
        )
        playerSelectionScreen(
            viewModel,
            onBack = { navController.popBackStack() },
            onNavigateToDistanceSetup = { navController.navigateToDistanceSetup() })
        distanceSetupScreen(
            viewModel,
            onBack = { navController.popBackStack() },
            onvNavigateToStopWatch = { navController.navigateToStopWatchScreen() })
        stopWatchScreen(viewModel, onBack = { navController.popBackStack() })
        playerMetricChartScreen(viewModel, onBack = { navController.popBackStack() })
    }
}