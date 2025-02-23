package com.matin.core.database.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.matin.core.database.PlayerEntity
import com.matin.core.database.PlayerWithSessionAndLaps
import com.matin.core.database.SessionEntity
import com.matin.core.database.TimeLapEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StopWatchDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlayer(player: PlayerEntity)

    @Query("SELECT * FROM player")
    suspend fun getAllPlayers(): List<PlayerEntity>

    @Query("SELECT * FROM player WHERE id = :playerId Limit 1")
    suspend fun getPlayerById(playerId: String): PlayerEntity?

    @Query("DELETE FROM player WHERE id = :playerId")
    suspend fun deletePlayerById(playerId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaps(laps: List<TimeLapEntity>)

    @Transaction
    @Query("SELECT * FROM player WHERE id = :playerId")
    suspend fun getPlayerWithSessionsAndLapsByPlayerId(playerId: Int): PlayerWithSessionAndLaps

    @Transaction
    @Query(
        """
    SELECT * FROM player
    WHERE id = (SELECT playerId FROM session WHERE id = :sessionId)
"""
    )
    suspend fun getPlayerWithSessionsAndLapsBySessionId(sessionId: String): PlayerWithSessionAndLaps


    @Transaction
    @Query("SELECT * FROM player")
    suspend fun getAllPlayersWithSessionsAndLaps(): List<PlayerWithSessionAndLaps>

    @Query("SELECT * FROM session")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Transaction
    suspend fun addPlayerSession(
        player: PlayerEntity,
        session: SessionEntity,
        laps: List<TimeLapEntity>
    ) {
        try {
            insertPlayer(player)
            insertSession(session)
            insertLaps(laps)
        } catch (e: Exception) {
            Log.e("addPlayerSession", "Failed to add player session: ${e.message}", e)
        }
    }

}