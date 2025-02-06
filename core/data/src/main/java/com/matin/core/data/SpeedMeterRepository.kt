package com.matin.core.data

import com.matin.model.Players

interface SpeedMeterRepository {
    suspend fun getPlayers(): Players
}