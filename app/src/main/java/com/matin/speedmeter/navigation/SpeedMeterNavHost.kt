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
import com.matin.feature.stopwatch.navigation.navigateToPlayerSelectionScreen
import com.matin.feature.stopwatch.navigation.playerSelectionScreen

@Composable
fun SpeedMeterNavHost() {
    val navController = rememberNavController()
    val viewModel: StopWatchSharedViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = LEADER_BOARD_ROUTE) {
        leaderBoardScreenRoute(viewModel, { navController.navigateToPlayerSelectionScreen() })
        playerSelectionScreen(viewModel, { navController.navigateToDistanceSetup() })
        distanceSetupScreen(viewModel) { /*navController.navigateToStopWatch()*/ }
    }
}