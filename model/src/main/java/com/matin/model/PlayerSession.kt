package com.matin.model


data class PlayerSession(
    val playerId: String,
    val sessionId: String,
    val fullName: String,
    val imageUrl: String,
    val distance: Float,
    val laps: List<TimeLap>
)
