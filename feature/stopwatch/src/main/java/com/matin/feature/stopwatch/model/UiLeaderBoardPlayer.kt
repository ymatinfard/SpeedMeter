package com.matin.feature.stopwatch.model

data class UiLeaderBoardPlayer(val id: Int, val fullName: String, val peakSpeed: Float, val laps: List<TimeLap>, val imageUrl: String, val distance: Float)
