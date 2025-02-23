package com.matin.feature.stopwatch.managers

import com.matin.feature.stopwatch.model.UiPlayerSelection
import com.matin.model.PlayerSession
import com.matin.model.TimeLap
import javax.inject.Inject

class PlayerSessionManager @Inject constructor() {
    fun createPlayerSession(
        player: UiPlayerSelection?,
        distance: Float,
        laps: List<TimeLap>
    ): PlayerSession {
        val playerId = player?.id ?: "0"
        return PlayerSession(
            playerId = playerId,
            sessionId = generateSessionId(playerId),
            fullName = player?.fullName ?: "",
            laps = laps,
            imageUrl = player?.imageUrl ?: "",
            distance = distance
        )
    }

    private fun generateSessionId(playerId: String): String {
        val randomSuffix = (100000..999999).random()
        return "$playerId-$randomSuffix"
    }
}