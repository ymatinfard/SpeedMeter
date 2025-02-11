package com.matin.core.data

import app.cash.turbine.test
import com.matin.core.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class DataAccessManagerTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataAccessManager: DataAccessManager<String, String>

    private val fetchFromNetwork: suspend (String) -> String = mockk()
    private val fetchFromMemory: (String) -> String? = mockk()
    private val saveToMemory: (String, String) -> Unit = mockk(relaxed = true)
    private val fetchFromStorage: suspend (String) -> String? = mockk()
    private val saveToStorage: suspend (String, String) -> Unit = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        dataAccessManager = DataAccessManager(
            ioDispatcher = testDispatcher,
            fetchFromNetwork = fetchFromNetwork,
            fetchFromMemory = fetchFromMemory,
            saveToMemory = saveToMemory,
            fetchFromStorage = fetchFromStorage,
            saveToStorage = saveToStorage
        )
    }

    @Test
    fun `observe should emit data from memory if available and not force reload`() = runTest {
        val params = "testParams"
        val cachedData = "cachedData"
        coEvery { fetchFromMemory(params) } returns cachedData

        dataAccessManager.observe(params, forceReload = false).test {
            val emittedData = awaitItem()
            assertEquals(cachedData, emittedData.content)
            assertEquals(false, emittedData.loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observe should fetch data from storage and save it in memory if not in memory and emit it`() =
        runTest {
            val params = "testParams"
            val networkData = "networkData"
            val storageData = "storageData"
            coEvery { fetchFromMemory(params) } returns null
            coEvery { fetchFromStorage(params) } returns storageData
            coEvery { fetchFromNetwork(params) } returns networkData

            dataAccessManager.observe(params, forceReload = true).test {
                assertEquals(true, awaitItem().loading)
                assertEquals(storageData, awaitItem().content)
                coVerify { saveToMemory(params, networkData) }
                coVerify { saveToStorage(params, networkData) }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `observe should fetch data from network and save it if not in storage and memory`() =
        runTest {
            val params = "testParams"
            val networkData = "networkData"
            coEvery { fetchFromMemory(params) } returns null
            coEvery { fetchFromStorage(params) } returns null
            coEvery { fetchFromNetwork(params) } returns networkData

            dataAccessManager.observe(params, forceReload = true).test {
                assertEquals(true, awaitItem().loading)
                assertEquals(networkData, awaitItem().content)
                coVerify { saveToMemory(params, networkData) }
                coVerify { saveToStorage(params, networkData) }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `observe should fetch data from network if forceReload is true`() = runTest {
        val params = "testParams"
        val networkData = "networkData"
        val storageData = "storageData"
        coEvery { fetchFromMemory(params) } returns null
        coEvery { fetchFromStorage(params) } returns storageData
        coEvery { fetchFromNetwork(params) } returns networkData

        dataAccessManager.observe(params, forceReload = true).test {
            assertEquals(true, awaitItem().loading)
            assertEquals(storageData, awaitItem().content)
            assertEquals(networkData, awaitItem().content)
            coVerify { saveToMemory(params, networkData) }
            coVerify { saveToStorage(params, networkData) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observe should emit error if network fetch fails`() = runTest {
        val params = "testParams"
        val exception = RuntimeException("Network error")
        coEvery { fetchFromMemory(params) } returns null
        coEvery { fetchFromStorage(params) } returns null
        coEvery { fetchFromNetwork(params) } throws exception

        dataAccessManager.observe(params, forceReload = true).test {
            assertEquals(true, awaitItem().loading)
            assertEquals(exception.message, awaitItem().error?.message)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
