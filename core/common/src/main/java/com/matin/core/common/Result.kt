package com.matin.core.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

sealed interface Result<out Domain> {
    data class Success<Domain>(val data: Domain) : Result<Domain>
    data class Error<Domain>(val throwable: Throwable, val data: Domain? = null) : Result<Domain>
    data object Loading : Result<Nothing>
}

fun <Domain> Flow<Data<Domain>>.asResult(): Flow<Result<Domain>> =
    map {
        when {
            it.loading -> Result.Loading
            // We may have cache data but with error(network/db), therefore, we can emit error with cache data
            it.error != null -> Result.Error(it.error, it.content)
            it.content != null -> Result.Success(it.content)
            else -> Result.Error(Throwable("Unknown error"))
        }
    }.catch { emit(Result.Error(it)) }