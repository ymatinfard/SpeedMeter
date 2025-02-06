package com.matin.core.data

import com.matin.core.network.SpeedMeterApi
import com.matin.core.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class SpeedMeterRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private val speedMeterApi: SpeedMeterApi = mockk()
    private val repository = SpeedMeterRepositoryImpl(speedMeterApi, testDispatcher)

    @get:Rule
    val rule = MainDispatcherRule(testDispatcher)

    @Test
    fun `getPlayers should return players from network`() = runTest(testDispatcher) {

        val apiResponse = fakeNetworkPlayers

        coEvery { speedMeterApi.getPlayers() } returns apiResponse

        val result = repository.getPlayers()

        assertEquals(1, result.players.size)
        assertEquals("Mr Yousef Matinfard", result.players[0].name.fullName)

        coVerify(exactly = 1) { speedMeterApi.getPlayers() }
    }
}