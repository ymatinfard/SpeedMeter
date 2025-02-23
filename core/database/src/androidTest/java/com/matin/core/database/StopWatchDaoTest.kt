package com.matin.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.matin.core.database.dao.StopWatchDao
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StopWatchDaoTest {

    private lateinit var stopWatchDao: StopWatchDao
    private lateinit var db: SpeedMeterDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            SpeedMeterDatabase::class.java
        ).build()
        stopWatchDao = db.stopWatchDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertPlayer_whenPlayerIsInserted_playerExists() = runTest {
        val player = PlayerEntity(id = "123", fullName = "Mr Yousef Matinfard", imageUrl = "")

        stopWatchDao.insertPlayer(player)

        val retrievedPlayer = stopWatchDao.getPlayerById("123")
        assertEquals(player, retrievedPlayer)
    }

    @Test
    fun insertPlayer_whenDuplicatePlayerIsInserted_playerIsIgnored() = runTest {
        val player = PlayerEntity(id = "123", fullName = "Mr Yousef Matinfard", imageUrl = "")

        stopWatchDao.insertPlayer(player)
        stopWatchDao.insertPlayer(player) // Duplicate insert

        val retrievedPlayer = stopWatchDao.getPlayerById("123")
        assertEquals(player, retrievedPlayer) // Ensure the original player is still present
    }

    @Test
    fun getPlayerById_whenPlayerDoesNotExist_returnsNull() = runTest {
        val retrievedPlayer = stopWatchDao.getPlayerById("999") // Non-existent ID

        assertNull(retrievedPlayer)
    }

    @Test
    fun deletePlayerById_whenPlayerIsDeleted_playerNoLongerExists() = runTest {
        val player = PlayerEntity(id = "123", fullName = "Mr Yousef Matinfard", imageUrl = "")
        stopWatchDao.insertPlayer(player)

        stopWatchDao.deletePlayerById("123")

        val retrievedPlayer = stopWatchDao.getPlayerById("123")
        assertNull(retrievedPlayer)
    }

    @Test
    fun getPlayerWithSessionsAndLaps_whenDataExists_returnsPlayerWithRelationsByPlayerId() = runTest {
        val player = PlayerEntity(id = "123", fullName = "Mr Yousef Matinfard", imageUrl = "")
        val session = SessionEntity(id = "123-22222", playerId = "123", distance = 12f)
        val laps = listOf(TimeLapEntity(id = 1, sessionId = "123-22222", lapTime = 1000L, totalTime = 2000L, lapNumber = 1, playerId = "123"))

        stopWatchDao.insertPlayer(player)
        stopWatchDao.insertSession(session)
        stopWatchDao.insertLaps(laps)

        val result = stopWatchDao.getPlayerWithSessionsAndLapsByPlayerId(123)

        assertEquals(player, result.player)
        assertEquals(session, result.sessionWithLaps.first().session)
        assertEquals(laps, result.sessionWithLaps.first().laps)
    }

    @Test
    fun getAllPlayersWithSessionsAndLaps_whenDataExists_returnsPlayersWithRelations() = runTest {
        val player1 = PlayerEntity(id = "1", fullName = "Player 1", imageUrl = "")
        val player2 = PlayerEntity(id = "2", fullName = "Player 2", imageUrl = "")
        val session1 = SessionEntity(id = "1-2222", playerId = "1", distance = 20f)
        val session2 = SessionEntity(id = "2-4444", playerId = "2", distance = 30f)
        val laps1 = listOf(TimeLapEntity(id = 1, sessionId = "1-2222", lapTime = 1000, totalTime = 2000, lapNumber = 1, playerId = "1"))
        val laps2 = listOf(TimeLapEntity(id = 2, sessionId = "2-4444", lapTime = 2000, totalTime = 4000, lapNumber = 1, playerId = "2"))

        stopWatchDao.insertPlayer(player1)
        stopWatchDao.insertPlayer(player2)
        stopWatchDao.insertSession(session1)
        stopWatchDao.insertSession(session2)
        stopWatchDao.insertLaps(laps1)
        stopWatchDao.insertLaps(laps2)

        val result = stopWatchDao.getAllPlayersWithSessionsAndLaps()

        assertEquals(2, result?.size)

        // Verify player 1 and their sessions/laps
        val playerWithRelations1 = result?.find { it.player.id == "1" }
        assertEquals(player1, playerWithRelations1?.player)
        assertEquals(session1, playerWithRelations1?.sessionWithLaps?.first()?.session)
        assertEquals(laps1, playerWithRelations1?.sessionWithLaps!!.first().laps)

        // Verify player 2 and their sessions/laps
        val playerWithRelations2 = result.find { it.player.id == "2" }
        assertEquals(player2, playerWithRelations2?.player)
        assertEquals(session2, playerWithRelations2?.sessionWithLaps!!.first().session)
        assertEquals(laps2, playerWithRelations2.sessionWithLaps.first().laps)
    }

    @Test
    fun getAllPlayers_whenPlayersExist_returnsAllPlayers() = runTest {
        val player1 = PlayerEntity(id = "1", fullName = "Player 1", imageUrl = "")
        val player2 = PlayerEntity(id = "2", fullName = "Player 2", imageUrl = "")
        stopWatchDao.insertPlayer(player1)
        stopWatchDao.insertPlayer(player2)

        val allPlayers = stopWatchDao.getAllPlayers()

        assertEquals(2, allPlayers.size)
        assertEquals(player1, allPlayers[0])
        assertEquals(player2, allPlayers[1])
    }

    @Test
    fun deletePlayerById_whenPlayerHasSessionsAndLaps_associatedDataIsDeleted() = runTest {
        val player = PlayerEntity(id = "123", fullName = "Mr Yousef Matinfard", imageUrl = "")
        val session = SessionEntity(id = "123-6666", playerId = "123", distance = 12f)
        val laps = listOf(TimeLapEntity(id = 1, sessionId = "123-6666", lapTime = 1000, totalTime = 2000, lapNumber = 1, playerId = "123"))

        stopWatchDao.insertPlayer(player)
        stopWatchDao.insertSession(session)
        stopWatchDao.insertLaps(laps)

        stopWatchDao.deletePlayerById("123")

        val retrievedPlayer = stopWatchDao.getPlayerById("123")
        assertNull(retrievedPlayer)

        val retrievedSession = stopWatchDao.getPlayerWithSessionsAndLapsByPlayerId(123)
        assertNull(retrievedSession)
    }

    @Test
    fun getPlayerWithSessionsAndLaps_whenPlayerHasSessionsAndLaps_returnsPlayerWithRelationsByPlayerId() = runTest {
        val player = PlayerEntity(id = "123", fullName = "Mr Yousef Matinfard", imageUrl = "")
        val session = SessionEntity(id = "123-6666", playerId = "123", distance = 12f)
        val laps = listOf(TimeLapEntity(id = 1, sessionId = "123-6666", lapTime = 1000, totalTime = 2000, lapNumber = 1, playerId = "123"))

        stopWatchDao.insertPlayer(player)
        stopWatchDao.insertSession(session)
        stopWatchDao.insertLaps(laps)

        val result = stopWatchDao.getPlayerWithSessionsAndLapsBySessionId(sessionId = "123-6666")

        assertEquals(player, result?.player)
        assertEquals(session, result?.sessionWithLaps?.first()?.session)
    }
}