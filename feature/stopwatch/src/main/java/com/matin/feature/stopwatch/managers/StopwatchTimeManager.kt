package com.matin.feature.stopwatch.managers

import com.matin.core.common.TimeProvider
import javax.inject.Inject

class StopwatchTimeManager @Inject constructor(
    private val timeProvider: TimeProvider
) {
    private var stopwatchStartTime = 0L

    fun calculateCurrentTime(baseTime: Long): Long {
        return timeProvider.elapsedRealtime() - stopwatchStartTime
    }

    fun setStartTime(currentTime: Long) {
        stopwatchStartTime = timeProvider.elapsedRealtime() - currentTime
    }
}