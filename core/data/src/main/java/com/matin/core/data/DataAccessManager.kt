package com.matin.core.data

import com.matin.core.common.Data
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class DataAccessManager<Params : Any, Domain> @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val fetchFromNetwork: suspend (Params) -> Domain,
    private val fetchFromMemory: (Params) -> Domain? = { null },
    private val saveToMemory: (Params, Domain) -> Unit = { _, _ -> },
    private val fetchFromStorage: suspend (Params) -> Domain? = { null },
    private val saveToStorage: suspend (Params, Domain) -> Unit = { _, _ -> }
) {

    @OptIn(FlowPreview::class)
    @ExperimentalCoroutinesApi
    fun observe(params: Params, forceReload: Boolean): Flow<Data<Domain>> = flow {
        val cachedData = fetchFromMemory(params)
        val shouldLoad = cachedData == null || forceReload
        emitAll(
            fetchFromStorage(params, cachedData)
                .flatMapMerge { storageData ->
                    fetchFromNetworkIfNeeded(shouldLoad, storageData, params)
                }
                .onStart {
                    emit(Data(content = cachedData, loading = shouldLoad))
                }
                .distinctUntilChanged()
        )
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private suspend fun fetchFromNetworkIfNeeded(
        shouldLoad: Boolean,
        storageData: Data<Domain>,
        params: Params
    ): Flow<Data<Domain>> {
        return if (shouldLoad) {
            val loadingData = storageData.copy(loading = true)
            flowOf(loadingData)
                .onCompletion {
                    if (it == null) emitAll(
                        merge(
                            fetchFromNetwork(params, storageData.content),
                        )
                    )
                }
        } else {
            flowOf(storageData)
        }
    }

    private suspend fun fetchFromStorage(
        params: Params,
        cachedData: Domain?
    ): Flow<Data<Domain>> =
        if (cachedData != null) {
            flowOf(Data(cachedData))
        } else {
            flow {
                val storageData = fetchFromStorage(params)
                storageData?.let {
                    saveToMemory(params, it)
                }
                emit(Data(content = storageData))
            }.flowOn(ioDispatcher)
                .catch { throwable ->
                    val loadingErrorData = Data<Domain>(error = throwable, loading = true)
                    emit(loadingErrorData)
                }
        }

    private suspend fun fetchFromNetwork(
        params: Params,
        cachedData: Domain?
    ): Flow<Data<Domain>> {
        return flow {
            val networkData = fetchFromNetwork(params)
            saveToMemory(params, networkData)
            saveToStorage(params, networkData)
            emit(Data(content = networkData))
        }.flowOn(ioDispatcher)
            .catch { error ->
                val errorData = Data(content = cachedData, error = error)
                emit(errorData)
            }
    }
}
