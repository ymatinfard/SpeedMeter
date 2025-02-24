package com.matin.core.data

import com.matin.core.common.Data
import com.matin.core.database.SessionEntity
import com.matin.model.Params
import com.matin.model.PlayerSession
import com.matin.model.Players
import kotlinx.coroutines.flow.Flow

interface SpeedMeterRepository {
    fun getPlayers(params: Params, forceLoad: Boolean = false): Flow<Data<Players>>
    suspend fun sync()
    suspend fun createCSVFile()
    suspend fun addPlayerSessionToDb(session: PlayerSession)
    fun getPlayersSessionsFromDB(): Flow<Data<List<PlayerSession>>>
    fun getPlayerSession(sessionId: String):  Flow<Data<PlayerSession>>
    fun observeSessionChanges(): Flow<List<SessionEntity>>
}