package com.matin.feature.stopwatch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matin.core.common.Result.Error
import com.matin.core.common.Result.Success
import com.matin.core.common.asResult
import com.matin.core.data.SpeedMeterRepository
import com.matin.feature.stopwatch.model.PlayerMetricChartUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlayerMetricChartViewModel @Inject constructor(
    private val repository: SpeedMeterRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sessionId = savedStateHandle["id"] ?: "0"

    val uiState = repository.getPlayerSession(sessionId).asResult().map {
        when (it) {
            is Success -> PlayerMetricChartUiState.Success(it.data)
            is Error -> PlayerMetricChartUiState.Error
            else -> PlayerMetricChartUiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = PlayerMetricChartUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000)
    )
}