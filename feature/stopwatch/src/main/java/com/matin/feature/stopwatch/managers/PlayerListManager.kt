package com.matin.feature.stopwatch.managers

import com.matin.core.common.Result
import com.matin.core.common.asResult
import com.matin.core.data.SpeedMeterRepository
import com.matin.feature.stopwatch.model.UiPlayerSelection
import com.matin.model.Params
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.matin.core.common.Result.*
import com.matin.feature.stopwatch.model.toUiPlayerSelection
import javax.inject.Inject

class PlayerListManager @Inject constructor(
    private val repository: SpeedMeterRepository
) {
    suspend fun fetchPlayerList(): Flow<Result<List<UiPlayerSelection>>> {
        return repository.getPlayers(Params("players"))
            .asResult()
            .map { result ->
                when (result) {
                    is Success -> Success(result.data.toUiPlayerSelection())
                    is Error -> Error(result.throwable, result.data?.toUiPlayerSelection())
                    else -> Loading
                }
            }
    }
}