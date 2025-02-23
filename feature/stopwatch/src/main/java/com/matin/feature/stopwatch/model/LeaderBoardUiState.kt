package com.matin.feature.stopwatch.model

import com.matin.core.common.SortOption
import com.matin.core.common.milliSecondToSecond
import com.matin.model.PlayerSession

sealed interface LeaderBoardUiState {
    data class Success(val data: LeaderBoarUiData) : LeaderBoardUiState
    data object Error : LeaderBoardUiState
    data object Loading : LeaderBoardUiState
}

data class LeaderBoarUiData(
    val players: List<UiLeaderBoardPlayer> = emptyList(),
    val sortOption: SortOption = SortOption.EXPLOSIVENESS
)

fun PlayerSession.toUiLeaderBoardPlayer() = UiLeaderBoardPlayer(
    id = playerId,
    sessionId = sessionId,
    fullName = fullName,
    peakSpeed = laps.maxOfOrNull { it.lapTime }?.milliSecondToSecond() ?: -1f,
    laps = laps,
    imageUrl = imageUrl,
    distance = distance
)