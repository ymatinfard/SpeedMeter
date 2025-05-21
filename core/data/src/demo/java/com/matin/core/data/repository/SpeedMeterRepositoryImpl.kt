package com.matin.core.data.repository

import android.util.Log
import com.matin.core.common.Data
import com.matin.core.data.DataAccessManagerFactory
import com.matin.core.data.FakeApiData
import com.matin.core.data.SpeedMeterRepository
import com.matin.core.data.di.Dispatcher
import com.matin.core.data.di.SpeedMeterDispatcher
import com.matin.core.data.toDomain
import com.matin.core.data.toPlayerEntity
import com.matin.core.data.toSessionEntity
import com.matin.core.data.toTimeLapListEntity
import com.matin.core.database.SessionEntity
import com.matin.core.database.dao.StopWatchDao
import com.matin.core.network.SpeedMeterApi
import com.matin.model.Params
import com.matin.model.PlayerSession
import com.matin.model.Players
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SpeedMeterRepositoryImpl @Inject constructor(
    private val speedMeterApi: SpeedMeterApi,
    private val dataAccessManagerFactory: DataAccessManagerFactory<Players>,
    private val db: StopWatchDao,
    @Dispatcher(SpeedMeterDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
) : SpeedMeterRepository {

    private val memoryCache = HashMap<String, Players>()

    private val dataAccessManager = dataAccessManagerFactory.create<Params, Players>(
        ioDispatcher = ioDispatcher,
        fetchFromNetwork = { fetchPlayers() },
        fetchFromMemory = { fetchFromMemory(it) },
        saveToMemory = { params, domain -> saveToMemory(params, domain) })

    override fun getPlayers(params: Params, forceLoad: Boolean): Flow<Data<Players>> =
        dataAccessManager.observe(params = params, forceReload = forceLoad)

    override suspend fun sync() {
        // getLocalData()
        // sendToServer
        Log.d("SpeedMeter", "sync-data")
        delay(2000)
    }

    override suspend fun createCSVFile() {
        Log.d("SpeedMeter", "create csv file")
        delay(5000)
    }

    private suspend fun fetchPlayers(): Players {
        return FakeApiData.getDomainMappedPlayers()
    }

    private fun fetchFromMemory(params: Params): Players? {
        return memoryCache[params.id]
    }

    private fun saveToMemory(params: Params, players: Players) {
        memoryCache[params.id] = players
    }

    /**
     * Adds a new player session to the database along with its related entities.
     *
     * This method handles the conversion and storage of a PlayerSession object into its corresponding
     * database entities (Player, Session, and TimeLap) using database transactions. The operation is
     * performed on the IO dispatcher to prevent blocking the main thread.
     *
     * Note: This operation is atomic - either all entities are saved successfully, or none are saved.
     */
    override suspend fun addPlayerSessionToDb(session: PlayerSession) = withContext(ioDispatcher) {
        db.addPlayerSession(
            session.toPlayerEntity(),
            session.toSessionEntity(),
            session.toTimeLapListEntity()
        )
    }

    /**
     * Retrieves all player sessions with their associated data from the database.
     * @return Flow of player sessions list with success/failure status
     */
    override fun getPlayersSessionsFromDB(): Flow<Data<List<PlayerSession>>> = flow {
        val playersWithRelations = db.getAllPlayersWithSessionsAndLaps()
        val playersSessions = playersWithRelations.map { it.toDomain() }.flatten()
        emit(Data(playersSessions))
    }.catch {
        emit(Data(content = emptyList(), error = it))
    }

    /**
     * Retrieves a specific player session by its session ID, including all related data.
     *
     * This method queries the database for a player session and its associated data
     * using the provided session ID. The result is wrapped in a Data class to handle
     * both success and error cases.
     * @param sessionId The unique identifier of the session to retrieve
     */
    override fun getPlayerSession(sessionId: String): Flow<Data<PlayerSession>> =
        flow {
            emit(
                Data(
                    content = db.getPlayerWithSessionsAndLapsBySessionId(sessionId)
                        .toDomain()
                        .firstOrNull()
                )
            )
        }.catch {
            emit(Data(content = null, error = it))
        }

    /**
     * Observes and emits changes to all sessions in the database in real-time.
     *
     * This method creates a continuous flow that monitors the sessions table and emits
     * updates whenever changes occur (insert or delete operations). The flow
     * remains active as long as there are active collectors.
     * */
    override fun observeSessionChanges(): Flow<List<SessionEntity>> = db.getAllSessions()
}