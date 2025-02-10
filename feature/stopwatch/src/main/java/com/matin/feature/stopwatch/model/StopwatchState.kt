package com.matin.feature.stopwatch.model

data class StopwatchState(
    val timeInMillis: Long = 0L,
    val isRunning: Boolean = false,
    val laps: List<TimeLap> = emptyList()
)

data class TimeLap(
    val id: Int,
    val lapTime: Long,
    val totalTime: Long,
)