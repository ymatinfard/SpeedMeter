package com.matin.feature.stopwatch.model

import com.matin.core.common.SortOption


data class LeaderBoardUiState(
    val players: List<UiLeaderBoardPlayer> = emptyList(),
    val sortOption: SortOption = SortOption.EXPLOSIVENESS
)