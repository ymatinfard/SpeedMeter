package com.matin.feature.stopwatch.managers

import com.matin.core.common.Result
import com.matin.core.common.SortOption
import com.matin.core.common.asResult
import com.matin.core.data.SpeedMeterRepository
import com.matin.feature.stopwatch.model.LeaderBoarUiData
import com.matin.feature.stopwatch.model.LeaderBoardUiState
import com.matin.feature.stopwatch.model.UiLeaderBoardPlayer
import com.matin.feature.stopwatch.model.toUiLeaderBoardPlayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LeaderboardManager @Inject constructor(
    private val repository: SpeedMeterRepository
) {
    suspend fun generateLeaderboardState(sortOption: SortOption): Flow<LeaderBoardUiState> =
        repository.getPlayersSessions().asResult().map {
            when (it) {
                is Result.Success -> {
                    val players = it.data.map { it.toUiLeaderBoardPlayer() }
                    LeaderBoardUiState.Success(
                        data = LeaderBoarUiData(
                            players = players.sortedWith(createLeaderboardComparator(sortOption)),
                            sortOption = sortOption
                        )
                    )
                }

                is Result.Loading -> LeaderBoardUiState.Loading
                is Result.Error -> LeaderBoardUiState.Error
            }

        }
}

private fun createLeaderboardComparator(option: SortOption): Comparator<UiLeaderBoardPlayer> {
    return when (option) {
        SortOption.EXPLOSIVENESS -> compareByDescending { it.peakSpeed }
        SortOption.ENDURANCE -> compareByDescending { it.laps.size }
    }
}