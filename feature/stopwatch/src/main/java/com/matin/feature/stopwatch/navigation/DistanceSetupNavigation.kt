package com.matin.feature.stopwatch.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.matin.feature.stopwatch.DistanceSetupScreen
import com.matin.feature.stopwatch.StopWatchSharedViewModel

const val DISTANCE_SETUP_ROUTE = "distance_setup_route"

fun NavController.navigateToDistanceSetup() = navigate(DISTANCE_SETUP_ROUTE)

fun NavGraphBuilder.distanceSetupScreen(viewModel: StopWatchSharedViewModel, onBack: () -> Unit, onvNavigateToStopWatch: () -> Unit) {
    composable(DISTANCE_SETUP_ROUTE) {
        DistanceSetupScreen(viewModel, onBack, onvNavigateToStopWatch)
    }
}