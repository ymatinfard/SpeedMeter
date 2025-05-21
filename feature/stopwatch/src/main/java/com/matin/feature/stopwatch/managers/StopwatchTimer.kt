package com.matin.feature.stopwatch.managers

import android.os.SystemClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchTimer @Inject constructor() {
    private var isRunning = false
    private val updateInterval = 20L

    private var _elapsedTime = MutableStateFlow<Long>(0L)
    val elapsedTime = _elapsedTime.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Default)
    private var updateJob: Job? = null

    private fun start() {
        val baseTime = SystemClock.elapsedRealtime() - _elapsedTime.value
        updateJob = scope.launch {
            while (isActive) {
                val now = SystemClock.elapsedRealtime()
                _elapsedTime.value = now - baseTime
                delay(updateInterval)
            }
        }
    }

    fun toggleTimer() {
        if (isRunning) {
            isRunning = false
            updateJob?.cancel()
        } else {
            isRunning = true
            start()
        }
    }

    fun reset() {
        updateJob?.cancel()
        _elapsedTime.value = 0L
        isRunning = false
    }

    fun isRunning() = isRunning
}