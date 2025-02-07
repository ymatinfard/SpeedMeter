package com.matin.feature.stopwatch

import androidx.lifecycle.ViewModel
import com.matin.feature.stopwatch.model.UiPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class StopWatchSharedViewModel @Inject constructor() : ViewModel() {
    var leaderBoardUiState = MutableStateFlow(LeaderBoardUiState())
        private set

    fun onSortOptionSelected(sortOption: SortOption) {
        leaderBoardUiState.update { currentState ->
            val sortedPlayers = currentState.players.sortedWith(getSortComparator(sortOption))

            currentState.copy(
                players = sortedPlayers,
                sortOption = sortOption
            )
        }
    }

    fun addPlayer(newPlayer: UiPlayer) {
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

    private fun getSortComparator(selectedSortOption: SortOption): Comparator<UiPlayer> {
        return when (selectedSortOption) {
            SortOption.EXPLOSIVENESS -> compareByDescending { it.peakSpeed }
            SortOption.ENDURANCE -> compareByDescending { it.laps }
        }
    }
}

data class LeaderBoardUiState(
    val players: List<UiPlayer> = emptyList(),
    val sortOption: SortOption = SortOption.EXPLOSIVENESS
)