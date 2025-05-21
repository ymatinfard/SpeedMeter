package com.matin.feature.stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matin.core.common.Result
import com.matin.core.common.SortOption
import com.matin.core.data.SpeedMeterRepository
import com.matin.feature.stopwatch.managers.LapManager
import com.matin.feature.stopwatch.managers.LeaderboardManager
import com.matin.feature.stopwatch.managers.PlayerListManager
import com.matin.feature.stopwatch.managers.PlayerSessionManager
import com.matin.feature.stopwatch.managers.StopwatchTimer
import com.matin.feature.stopwatch.model.CurrentSelectedPlayer
import com.matin.feature.stopwatch.model.LeaderBoardUiState
import com.matin.feature.stopwatch.model.StopwatchState
import com.matin.feature.stopwatch.model.UiPlayerSelection
import com.matin.worker.csv.CSVExporter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StopwatchSharedViewModel @Inject constructor(
    private val repository: SpeedMeterRepository,
    private val csvExporter: CSVExporter,
    private val stopwatchTimer: StopwatchTimer,
    private val lapManager: LapManager,
    private val leaderboardManager: LeaderboardManager,
    private val playerSessionManager: PlayerSessionManager,
    private val playerListManager: PlayerListManager
) : ViewModel() {

    private val _stopwatchState = MutableStateFlow(StopwatchState())
    val stopwatchState: StateFlow<StopwatchState> = _stopwatchState.asStateFlow()

    private val _selectedPlayer = MutableStateFlow(CurrentSelectedPlayer())
    val selectedPlayer: StateFlow<CurrentSelectedPlayer> = _selectedPlayer.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.EXPLOSIVENESS)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val playerListRetryTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val playerList: StateFlow<Result<List<UiPlayerSelection>>> = playerListRetryTrigger
        .onStart { emit(Unit) }
        .flatMapLatest { playerListManager.fetchPlayerList() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(FLOW_SUBSCRIPTION_TIMEOUT_MS),
            initialValue = Result.Loading
        )

    val leaderboard: StateFlow<LeaderBoardUiState> = repository.observeSessionChanges()
        .flatMapLatest {
            sortOption.flatMapLatest { sortOption ->
                leaderboardManager.generateLeaderboardState(
                    sortOption
                )
            }
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(FLOW_SUBSCRIPTION_TIMEOUT_MS),
            initialValue = LeaderBoardUiState.Loading
        )

    init {
        observeElapsedTime()
    }

    fun updateSortOption(option: SortOption) {
        _sortOption.value = option
    }

    fun updateSelectedPlayer(player: UiPlayerSelection? = null, distance: Float? = null) {
        _selectedPlayer.update { current ->
            current.copy(
                player = player ?: current.player,
                distance = distance ?: current.distance
            )
        }
    }

    fun recordLap() {
        val currentState = stopwatchState.value
        if (!currentState.isRunning) return

        val newLap = lapManager.createNewLap(currentState.timeInMillis, currentState.laps)
        _stopwatchState.update { it.copy(laps = it.laps + newLap) }
    }

    fun saveAndResetSession() {
        stopwatchTimer.reset()
        if (stopwatchState.value.laps.isEmpty()) {
            resetStopwatchUiState()
            return
        }

        viewModelScope.launch {
            val session = playerSessionManager.createPlayerSession(
                selectedPlayer.value.player,
                selectedPlayer.value.distance,
                stopwatchState.value.laps
            )
            repository.addPlayerSessionToDb(session)
            resetStopwatchUiState()
        }
    }

    fun retryLoadingPlayerList() {
        playerListRetryTrigger.tryEmit(Unit)
    }

    fun exportToCsv() {
        csvExporter.initCSVFileExporter()
    }

    fun toggleStopwatch() {
        stopwatchTimer.toggleTimer()
        _stopwatchState.update { it.copy(isRunning = stopwatchTimer.isRunning()) }
    }

    fun observeElapsedTime() {
        viewModelScope.launch {
            stopwatchTimer.elapsedTime.collect { elapsedTime ->
                _stopwatchState.update {
                    it.copy(timeInMillis = elapsedTime)
                }
            }
        }
    }

    private fun resetStopwatchUiState() {
        _stopwatchState.update { StopwatchState() }
    }

    companion object {
        private const val FLOW_SUBSCRIPTION_TIMEOUT_MS = 5_000L
    }
}