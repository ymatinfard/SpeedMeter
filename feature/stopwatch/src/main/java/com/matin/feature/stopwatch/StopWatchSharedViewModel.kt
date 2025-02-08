package com.matin.feature.stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matin.core.common.Result
import com.matin.core.common.asResult
import com.matin.core.data.SpeedMeterRepository
import com.matin.feature.stopwatch.model.CurrentSelectedPlayer
import com.matin.feature.stopwatch.model.LeaderBoardUiState
import com.matin.feature.stopwatch.model.StopwatchState
import com.matin.feature.stopwatch.model.TimeLap
import com.matin.feature.stopwatch.model.UiLeaderBoardPlayer
import com.matin.feature.stopwatch.model.UiPlayerSelection
import com.matin.feature.stopwatch.model.toUiPlayerSelection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class StopWatchSharedViewModel @Inject constructor(private val repository: SpeedMeterRepository) :
    ViewModel() {
    var leaderBoardUiState = MutableStateFlow(LeaderBoardUiState())
        private set

    var playerListState = repository.getPlayers().map { it.toUiPlayerSelection() }.asResult()
        .stateIn(
            viewModelScope,
            initialValue = Result.Loading,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    var currentSelectedPlayer = MutableStateFlow(CurrentSelectedPlayer())

    var stopWatchUiState = MutableStateFlow(StopwatchState())
        private set

    fun setSelectedSortOption(sortOption: SortOption) {
        leaderBoardUiState.update { currentState ->
            val sortedPlayers = currentState.players.sortedWith(getSortComparator(sortOption))

            currentState.copy(
                players = sortedPlayers,
                sortOption = sortOption
            )
        }
    }

    fun addPlayer(newPlayer: UiLeaderBoardPlayer) {
        leaderBoardUiState.update { currentState ->
            val updatedPlayersList = currentState.players + newPlayer
            val sortedPlayers = updatedPlayersList.sortedWith(
                getSortComparator(currentState.sortOption)
            )
            currentState.copy(
                players = sortedPlayers
            )
        }
    }

    private fun getSortComparator(selectedSortOption: SortOption): Comparator<UiLeaderBoardPlayer> {
        return when (selectedSortOption) {
            SortOption.EXPLOSIVENESS -> compareByDescending { it.peakSpeed }
            SortOption.ENDURANCE -> compareByDescending { it.laps }
        }
    }

    fun setCurrentSelectedPlayer(player: UiPlayerSelection? = null, distance: Double? = null) {
        require(player != null || distance != null) {
            "Either player or distance must be provided"
        }

        currentSelectedPlayer.update { currentPlayer ->
            currentPlayer.copy(
                player = player ?: currentPlayer.player,
                distance = distance ?: currentPlayer.distance
            )
        }
    }

    fun setSelectedPlayer(player: UiPlayerSelection) {
        currentSelectedPlayer.update { currentPlayer ->
            currentPlayer.copy(player = player)
        }
    }

    fun toggleTimer() {
        stopWatchUiState.update { it.copy(isRunning = !it.isRunning) }
    }

    fun addLap() {
        val currentState = stopWatchUiState.value
        if (!currentState.isRunning) return

        val newLap = TimeLap(
            lapCount = currentState.laps.size + 1,
            lapTime = currentState.timeInMillis - (currentState.laps.lastOrNull()?.totalTime ?: 0L),
            totalTime = currentState.timeInMillis
        )
        stopWatchUiState.update { it.copy(laps = it.laps + newLap) }
    }

    fun resetTimer() {
        stopWatchUiState.update {
            StopwatchState()
        }
    }

    fun updateTime() {
        stopWatchUiState.update { it.copy(timeInMillis = it.timeInMillis + 100L) }
    }
}