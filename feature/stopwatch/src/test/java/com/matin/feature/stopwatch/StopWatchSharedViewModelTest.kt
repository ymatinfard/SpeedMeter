package com.matin.feature.stopwatch

import app.cash.turbine.test
import com.matin.core.common.Result
import com.matin.core.data.SpeedMeterRepository
import com.matin.core.testing.MainDispatcherRule
import com.matin.core.testing.getFakePlayers
import com.matin.feature.stopwatch.model.UiLeaderBoardPlayer
import com.matin.feature.stopwatch.model.toUiPlayerSelection
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class StopWatchSharedViewModelTest {

    @get:Rule
    val rule = MainDispatcherRule()
    private lateinit var viewModel: StopWatchSharedViewModel
    private lateinit var repository: SpeedMeterRepository

    @Before
    fun setup() {
        repository = mockk<SpeedMeterRepository>()
        every { repository.getPlayers() } returns flowOf(getFakePlayers())

        viewModel = StopWatchSharedViewModel(repository)
    }

    @Test
    fun `sortPlayers should sort players based on selected sort option`() = runTest {

        val players = listOf(
            UiLeaderBoardPlayer("PlayerA", 120, 10, 0),
            UiLeaderBoardPlayer("PlayerB", 110, 15, 0),
            UiLeaderBoardPlayer("PlayerC", 130, 8, 0)
        )

        players.forEach { viewModel.addPlayer(it) }

        viewModel.setSelectedSortOption(SortOption.EXPLOSIVENESS)

        val expectedOrder = players.sortedByDescending { it.peakSpeed }

        viewModel.leaderBoardUiState.test {
            val actualPlayers = awaitItem().players
            assertEquals(expectedOrder[0], actualPlayers[0])
            assertEquals(expectedOrder[1], actualPlayers[1])
            assertEquals(expectedOrder[2], actualPlayers[2])
        }
    }

    @Test
    fun `playerList should emit players from repository and map it to UiPlayerSelection`() =
        runTest {
            val uiPlayerSelection = getFakePlayers().toUiPlayerSelection()

            advanceUntilIdle()

            viewModel.playerListState.test {
                val result = awaitItem()
                assertTrue(result is Result.Success)
                assertEquals(uiPlayerSelection, (result as Result.Success).data)
            }
        }

    @Test
    fun `setSelectedPlayer should update currentSelectedPlayer`() = runTest {
        val player = getFakePlayers().toUiPlayerSelection()[0]
        viewModel.setSelectedPlayer(player)

        viewModel.currentSelectedPlayer.test {
            val currentPlayer = awaitItem()
            assertEquals(player, currentPlayer.player)
        }
    }

    @Test
    fun `toggleTimer should toggle isRunning in stopwatchUiState`() = runTest {
        viewModel.toggleTimer()

        viewModel.stopWatchUiState.test {
            val stopwatchState = awaitItem()
            assertTrue(stopwatchState.isRunning)
        }
    }

    @Test
    fun `addLap should add a new lap to laps in stopwatchUiState`() = runTest {
        viewModel.toggleTimer()
        advanceUntilIdle()
        viewModel.addLap()
        viewModel.addLap()

        viewModel.stopWatchUiState.test {
            val stopwatchState = awaitItem()
            assertEquals(2, stopwatchState.laps.size)
        }
    }

    @Test
    fun `resetTimer should reset stopwatchUiState to initial state`() = runTest {
        viewModel.toggleTimer()
        advanceUntilIdle()
        viewModel.addLap()

        viewModel.resetTimer()

        viewModel.stopWatchUiState.test {
            val stopwatchState = awaitItem()
            assertEquals(0, stopwatchState.timeInMillis)
        }
    }
}