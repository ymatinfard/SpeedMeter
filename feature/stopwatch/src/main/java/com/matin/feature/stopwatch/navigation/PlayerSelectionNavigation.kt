package com.matin.feature.stopwatch.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.matin.feature.stopwatch.StopwatchSharedViewModel
import com.matin.feature.stopwatch.screen.PlayerSelectionScreen

const val PLAYER_SELECTION_ROUTE = "player_selection_route"

fun NavController.navigateToPlayerSelectionScreen() = navigate(PLAYER_SELECTION_ROUTE)

fun NavGraphBuilder.playerSelectionScreen(viewModel: StopwatchSharedViewModel, onBack: () -> Unit, onNavigateToDistanceSetup: () -> Unit) {
    composable(PLAYER_SELECTION_ROUTE) {
        PlayerSelectionScreen(viewModel, onBack, onNavigateToDistanceSetup)
    }
}