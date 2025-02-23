package com.matin.feature.stopwatch.managers

import com.matin.model.TimeLap
import javax.inject.Inject

class LapManager @Inject constructor() {
    fun createNewLap(currentTime: Long, existingLaps: List<TimeLap>): TimeLap {
        val lapNumber = existingLaps.size + 1
        val previousLapTotalTime = existingLaps.lastOrNull()?.totalTime ?: 0L
        return TimeLap(
            id = lapNumber,
            lapTime = currentTime - previousLapTotalTime,
            totalTime = currentTime
        )
    }
}