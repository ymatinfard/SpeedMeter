package com.matin.core.data

import android.util.Log
import com.matin.core.common.Data
import com.matin.core.data.di.Dispatcher
import com.matin.core.data.di.SpeedMeterDispatcher
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
        return speedMeterApi.getPlayers().toDomain()
    }

    private fun fetchFromMemory(params: Params): Players? {
        return memoryCache[params.id]
    }

    private fun saveToMemory(params: Params, players: Players) {
        memoryCache[params.id] = players
    }

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
    override fun getPlayersSessions(): Flow<Data<List<PlayerSession>>> = flow {
        val playersWithRelations = db.getAllPlayersWithSessionsAndLaps()
        val playersSessions = playersWithRelations?.map { it.toDomain() }?.flatten()
            ?: emptyList()
        emit(Data(playersSessions))
    }.catch {
        emit(Data(content = emptyList(), error = it))
    }

    override fun getPlayerSession(sessionId: String): Flow<Data<PlayerSession>> =
        flow {
            emit(
                Data(
                    content = db.getPlayerWithSessionsAndLapsBySessionId(sessionId)
                        ?.toDomain()
                        ?.firstOrNull()
                )
            )
        }.catch {
            emit(Data(content = null, error = it))
        }

    override fun observeSessionChanges(): Flow<List<SessionEntity>> = db.getAllSessions()
}