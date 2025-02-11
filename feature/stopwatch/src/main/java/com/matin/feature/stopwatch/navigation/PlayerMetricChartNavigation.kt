package com.matin.feature.stopwatch.navigation

import PlayerMetricChartScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.matin.feature.stopwatch.StopWatchSharedViewModel

const val PLAYER_METRIC_CHART_ROUTE = "player_metric_chart"

fun NavController.navigateToPlayerMetricChart(id: Int) = navigate("$PLAYER_METRIC_CHART_ROUTE/$id")

fun NavGraphBuilder.playerMetricChartScreen(
    viewModel: StopWatchSharedViewModel,
    onBack: () -> Unit
) {
    composable(
        "$PLAYER_METRIC_CHART_ROUTE/{id}",
        arguments = listOf(navArgument("id") { type = NavType.IntType })
    ) {
        val id = it.arguments?.getInt("id") ?: 0
        PlayerMetricChartScreen(id, viewModel, onBack)
    }
}