package com.matin.core.data

import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DataAccessManagerFactory<Domain> @Inject constructor() {
    fun <Params : Any, Domain> create(
        ioDispatcher: CoroutineDispatcher,
        fetchFromNetwork: suspend (Params) -> Domain,
        fetchFromMemory: (Params) -> Domain? = { null },
        saveToMemory: (Params, Domain) -> Unit = { _, _ -> },
        fetchFromStorage: suspend (Params) -> Domain? = { null },
        saveToStorage: suspend (Params, Domain) -> Unit = { _, _ -> }
    ): DataAccessManager<Params, Domain> {
        return DataAccessManager(
            ioDispatcher,
            fetchFromNetwork,
            fetchFromMemory,
            saveToMemory,
            fetchFromStorage,
            saveToStorage
        )
    }
}