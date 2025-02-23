package com.matin.feature.stopwatch.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.matin.feature.stopwatch.StopwatchSharedViewModel
import com.matin.feature.stopwatch.screen.DistanceSetupScreen

const val DISTANCE_SETUP_ROUTE = "distance_setup_route"

fun NavController.navigateToDistanceSetup() = navigate(DISTANCE_SETUP_ROUTE)

fun NavGraphBuilder.distanceSetupScreen(viewModel: StopwatchSharedViewModel, onBack: () -> Unit, onvNavigateToStopWatch: () -> Unit) {
    composable(DISTANCE_SETUP_ROUTE) {
        DistanceSetupScreen(viewModel, onBack, onvNavigateToStopWatch)
    }
}