package com.matin.core.data

import com.matin.core.common.Data
import com.matin.core.data.di.Dispatcher
import com.matin.core.data.di.SpeedMeterDispatcher
import com.matin.core.network.SpeedMeterApi
import com.matin.model.Params
import com.matin.model.Players
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SpeedMeterRepositoryImpl @Inject constructor(
    private val speedMeterApi: SpeedMeterApi,
    private val dataAccessManagerFactory: DataAccessManagerFactory<Players>,
    @Dispatcher(SpeedMeterDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : SpeedMeterRepository {

    private val memoryCache = HashMap<String, Players>()

    private val dataAccessManager = dataAccessManagerFactory.create<Params, Players>(
        ioDispatcher = ioDispatcher,
        fetchFromNetwork = { fetchPlayers() },
        fetchFromMemory = { fetchFromMemory(it) },
        saveToMemory = { params, domain -> saveToMemory(params, domain) })

    override fun getPlayers(params: Params, forceLoad: Boolean): Flow<Data<Players>> =
        dataAccessManager.observe(params = params, forceReload = forceLoad)

    private suspend fun fetchPlayers(): Players {
        return speedMeterApi.getPlayers().toDomain()
    }

    private fun fetchFromMemory(params: Params): Players? {
        return memoryCache[params.id]
    }

    private fun saveToMemory(params: Params, players: Players) {
        memoryCache[params.id] = players
    }
}