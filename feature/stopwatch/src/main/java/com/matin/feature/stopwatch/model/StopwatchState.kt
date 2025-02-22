package com.matin.feature.stopwatch.model

import com.matin.model.TimeLap

data class StopwatchState(
    val timeInMillis: Long = 0L,
    val isRunning: Boolean = false,
    val laps: List<TimeLap> = emptyList()
)