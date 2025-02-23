package com.matin.feature.stopwatch.navigation

import com.matin.feature.stopwatch.screen.PlayerMetricChartScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val PLAYER_METRIC_CHART_ROUTE = "player_metric_chart"

fun NavController.navigateToPlayerMetricChart(id: String) = navigate("$PLAYER_METRIC_CHART_ROUTE/$id")

fun NavGraphBuilder.playerMetricChartScreen(
    onBack: () -> Unit
) {
    composable(
        "$PLAYER_METRIC_CHART_ROUTE/{id}",
        arguments = listOf(navArgument("id") { type = NavType.StringType })
    ) {
        PlayerMetricChartScreen(onBack = onBack)
    }
}