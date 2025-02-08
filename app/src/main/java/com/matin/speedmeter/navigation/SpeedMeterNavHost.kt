package com.matin.speedmeter.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.matin.feature.stopwatch.StopWatchSharedViewModel
import com.matin.feature.stopwatch.navigation.STOP_WATCH_ROUTE
import com.matin.feature.stopwatch.navigation.distanceSetupScreen
import com.matin.feature.stopwatch.navigation.leaderBoardScreenRoute
import com.matin.feature.stopwatch.navigation.navigateToDistanceSetup
import com.matin.feature.stopwatch.navigation.navigateToPlayerSelectionScreen
import com.matin.feature.stopwatch.navigation.navigateToStopWatchScreen
import com.matin.feature.stopwatch.navigation.playerSelectionScreen
import com.matin.feature.stopwatch.navigation.stopWatchScreen

@Composable
fun SpeedMeterNavHost() {
    val navController = rememberNavController()
    val viewModel: StopWatchSharedViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = STOP_WATCH_ROUTE) {
        leaderBoardScreenRoute(viewModel, { navController.navigateToPlayerSelectionScreen() })
        playerSelectionScreen(viewModel, { navController.navigateToDistanceSetup() })
        distanceSetupScreen(viewModel) { navController.navigateToStopWatchScreen()}
        stopWatchScreen(viewModel)
    }
}