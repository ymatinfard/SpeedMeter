package com.matin.feature.stopwatch

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matin.core.common.Result
import com.matin.core.common.SortOption
import com.matin.core.common.asResult
import com.matin.core.data.SpeedMeterRepository
import com.matin.feature.stopwatch.model.CurrentSelectedPlayer
import com.matin.feature.stopwatch.model.LeaderBoardUiState
import com.matin.feature.stopwatch.model.StopwatchState
import com.matin.feature.stopwatch.model.TimeLap
import com.matin.feature.stopwatch.model.UiLeaderBoardPlayer
import com.matin.feature.stopwatch.model.UiPlayerSelection
import com.matin.feature.stopwatch.model.toUiPlayerSelection
import com.matin.model.Params
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StopWatchSharedViewModel @Inject constructor(private val repository: SpeedMeterRepository) :
    ViewModel() {

    var leaderBoardUiState = MutableStateFlow(LeaderBoardUiState())
        private set

    private val retryTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    var playerListUiState = retryTrigger.onStart { emit(Unit) }.flatMapLatest {
        repository.getPlayers(Params("players")).asResult().map {
            when (it) {
                is Result.Success -> Result.Success(it.data.toUiPlayerSelection())
                is Result.Error -> {
                    Result.Error(it.throwable, it.data?.toUiPlayerSelection())
                }

                else -> {
                    Result.Loading
                }
            }
        }
    }.stateIn(
        viewModelScope,
        initialValue = Result.Loading,
        started = SharingStarted.WhileSubscribed(5_000)
    )

    var currentSelectedPlayer = MutableStateFlow(CurrentSelectedPlayer())

    var stopWatchUiState = MutableStateFlow(StopwatchState())
        private set

    private var timerJob: Job? = null
    private var startTime = 0L

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
            SortOption.ENDURANCE -> compareByDescending { it.laps.size }
        }
    }

    fun setCurrentSelectedPlayer(player: UiPlayerSelection? = null, distance: Float? = null) {
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
        val currentState = stopWatchUiState.value
        if (currentState.isRunning) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        val currentTime = stopWatchUiState.value.timeInMillis
        startTime = SystemClock.elapsedRealtime() - currentTime

        timerJob = viewModelScope.launch {
            while (true) {
                stopWatchUiState.update {
                    it.copy(
                        timeInMillis = SystemClock.elapsedRealtime() - startTime,
                        isRunning = true
                    )
                }
                delay(WATCH_INTERVAL)
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        stopWatchUiState.update { it.copy(isRunning = false) }
    }

    fun addLap() {
        val currentState = stopWatchUiState.value
        if (!currentState.isRunning) return

        val newLap = TimeLap(
            id = currentState.laps.size + 1,
            lapTime = currentState.timeInMillis - (currentState.laps.lastOrNull()?.totalTime ?: 0L),
            totalTime = currentState.timeInMillis
        )
        stopWatchUiState.update { it.copy(laps = it.laps + newLap) }
    }

    fun saveSessionAndRest() {
        if (stopWatchUiState.value.laps.isEmpty()) return

        val laps = stopWatchUiState.value.laps
        val peakSpeed =
            laps.maxOfOrNull { currentSelectedPlayer.value.distance / it.lapTime } ?: -1f
        val player = UiLeaderBoardPlayer(
            fullName = currentSelectedPlayer.value.player?.fullName ?: "",
            peakSpeed = peakSpeed,
            laps = laps,
            imageUrl = currentSelectedPlayer.value.player?.imageUrl ?: ""
        )

        addPlayer(player)
        stopTimer()
        stopWatchUiState.update {
            StopwatchState()
        }
    }

    fun retryPlayerList() {
        retryTrigger.tryEmit(Unit)
    }
}

const val WATCH_INTERVAL = 20L
