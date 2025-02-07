package com.matin.feature.stopwatch

import app.cash.turbine.test
import com.matin.feature.stopwatch.model.UiPlayer
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StopWatchSharedViewModelTest {

    private lateinit var viewModel: StopWatchSharedViewModel

    @Before
    fun setup() {
        viewModel = StopWatchSharedViewModel()
    }

    @Test
    fun `sortPlayers should sort players based on selected sort option`() = runTest {

        val players = listOf(
            UiPlayer("PlayerA", 120, 10, 0),
            UiPlayer("PlayerB", 110, 15, 0),
            UiPlayer("PlayerC", 130, 8, 0)
        )

        players.forEach { viewModel.addPlayer(it) }

        viewModel.onSortOptionSelected(SortOption.EXPLOSIVENESS)

        val expectedOrder = players.sortedByDescending { it.peakSpeed }

        viewModel.leaderBoardUiState.test {
            val actualPlayers = awaitItem().players
            assertEquals(expectedOrder[0], actualPlayers[0])
            assertEquals(expectedOrder[1], actualPlayers[1])
            assertEquals(expectedOrder[2], actualPlayers[2])
        }
    }
}