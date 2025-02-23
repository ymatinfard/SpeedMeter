package com.matin.feature.stopwatch.model

import com.matin.model.TimeLap

data class UiLeaderBoardPlayer(val id: String, val sessionId: String, val fullName: String, val peakSpeed: Float, val laps: List<TimeLap>, val imageUrl: String, val distance: Float)
