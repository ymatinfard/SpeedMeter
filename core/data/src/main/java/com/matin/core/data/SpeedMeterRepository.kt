package com.matin.core.data

import com.matin.model.Players
import kotlinx.coroutines.flow.Flow

interface SpeedMeterRepository {
    fun getPlayers(): Flow<Players>
}