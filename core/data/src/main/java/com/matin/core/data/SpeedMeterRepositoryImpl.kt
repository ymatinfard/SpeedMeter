package com.matin.core.data

import com.matin.core.data.di.Dispatcher
import com.matin.core.data.di.SpeedMeterDispatcher
import com.matin.core.network.SpeedMeterApi
import com.matin.model.Players
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SpeedMeterRepositoryImpl(
    private val speedMeterApi: SpeedMeterApi,
    @Dispatcher(SpeedMeterDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : SpeedMeterRepository {
    override fun getPlayers(): Flow<Players> = flow {
        emit(speedMeterApi.getPlayers().toDomain())
    }.flowOn(ioDispatcher)
}