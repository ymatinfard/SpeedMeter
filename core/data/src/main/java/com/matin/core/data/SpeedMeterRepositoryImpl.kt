package com.matin.core.data

import com.matin.core.data.di.Dispatcher
import com.matin.core.data.di.SpeedMeterDispatcher
import com.matin.core.network.SpeedMeterApi
import com.matin.model.Players
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SpeedMeterRepositoryImpl(
    private val speedMeterApi: SpeedMeterApi,
    @Dispatcher(SpeedMeterDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : SpeedMeterRepository {
    override suspend fun getPlayers(): Players = withContext(ioDispatcher) {
        speedMeterApi.getPlayers().toDomain()
    }
}