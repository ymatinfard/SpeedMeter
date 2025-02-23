package com.matin.core.data

import com.matin.core.database.PlayerEntity
import com.matin.core.database.PlayerWithSessionAndLaps
import com.matin.core.database.SessionEntity
import com.matin.core.database.TimeLapEntity
import com.matin.model.PlayerSession
import com.matin.model.TimeLap

fun PlayerSession.toPlayerEntity() = PlayerEntity(
    id = playerId,
    fullName = fullName,
    imageUrl = imageUrl
)

fun PlayerSession.toTimeLapListEntity() = this.laps.map {
    TimeLapEntity(
        playerId = playerId,
        sessionId = sessionId,
        lapNumber = it.id,
        lapTime = it.lapTime,
        totalTime = it.totalTime
    )
}

fun PlayerSession.toSessionEntity() = SessionEntity(
    id = sessionId,
    playerId = playerId,
    distance = distance
)

fun PlayerWithSessionAndLaps.toDomain() = this.sessionWithLaps.map {
    PlayerSession(
        playerId = this.player.id,
        sessionId = it.session.id,
        fullName = this.player.fullName,
        imageUrl = this.player.imageUrl,
        distance = it.session.distance,
        laps = it.laps.map { lap ->
            TimeLap(
                id = lap.lapNumber,
                lapTime = lap.lapTime,
                totalTime = lap.totalTime
            )
        })
}