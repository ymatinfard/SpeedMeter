package com.matin.feature.stopwatch.model

import com.matin.model.PlayerSession

sealed interface PlayerMetricChartUiState {
    data class Success(val player: PlayerSession) : PlayerMetricChartUiState
    data object Error : PlayerMetricChartUiState
    data object Loading : PlayerMetricChartUiState
}