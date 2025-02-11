package com.matin.core.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error<T>(val throwable: Throwable, val data: T? = null) : Result<T>
    data object Loading : Result<Nothing>
}

fun <T> Flow<Data<T>>.asResult(): Flow<Result<T>> =
    map {
        when {
            it.loading -> Result.Loading
            // We may have cache data but with error(network/db), therefore, we can emit error with cache data
            it.error != null -> Result.Error(it.error, it.content)
            it.content != null -> Result.Success(it.content)
            else -> Result.Error(Throwable("Unknown error"))
        }
    }.catch { emit(Result.Error(it)) }