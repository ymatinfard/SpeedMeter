package com.matin.feature.stopwatch.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.matin.feature.stopwatch.StopWatchScreen
import com.matin.feature.stopwatch.StopWatchSharedViewModel

const val STOP_WATCH_ROUTE = "stop_watch_route"

fun NavController.navigateToStopWatchScreen() {
    navigate(STOP_WATCH_ROUTE) {
        popUpTo(DISTANCE_SETUP_ROUTE) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.stopWatchScreen(viewModel: StopWatchSharedViewModel) {
    composable(STOP_WATCH_ROUTE) {
        StopWatchScreen(viewModel)
    }
}