package com.matin.feature.stopwatch

import app.cash.turbine.test
import com.matin.core.common.Result
import com.matin.core.common.SortOption
import com.matin.core.common.TimeProvider
import com.matin.core.data.SpeedMeterRepository
import com.matin.core.testing.MainDispatcherRule
import com.matin.core.testing.getFakePlayers
import com.matin.feature.stopwatch.model.TimeLap
import com.matin.feature.stopwatch.model.UiLeaderBoardPlayer
import com.matin.feature.stopwatch.model.toUiPlayerSelection
import com.matin.model.Params
import com.matin.sync.csv.CSVExporter
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class StopWatchSharedViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: StopWatchSharedViewModel

    private val repository = mockk<SpeedMeterRepository>(relaxed = true)
    private val csvExporter = mockk<CSVExporter>(relaxed = true)
    private val timeProvider = mockk<TimeProvider>()

    private val testPlayers = listOf(
        UiLeaderBoardPlayer(id = 1, "PlayerA", 120f, createTestLaps(3), "", 30f),
        UiLeaderBoardPlayer(id = 2, "PlayerB", 110f, createTestLaps(2), "", 30f),
        UiLeaderBoardPlayer(id = 3, "PlayerC", 130f, createTestLaps(1), "", 30f)
    )

    @Before
    fun setup() {
        setupMocks()
        initViewModel()
    }

    private fun setupMocks() {
        every { repository.getPlayers(Params("players")) } returns flowOf(getFakePlayers())
        every { timeProvider.elapsedRealtime() } returns DEFAULT_TIME
    }

    private fun initViewModel() {
        viewModel = StopWatchSharedViewModel(repository, csvExporter, timeProvider)
    }

    @Test
    fun `verify initial state`() = runTest {
        with(viewModel) {
            assertEquals(0L, stopWatchUiState.value.timeInMillis)
            assertFalse(stopWatchUiState.value.isRunning)
            assertTrue(stopWatchUiState.value.laps.isEmpty())
            assertTrue(leaderBoardUiState.value.players.isEmpty())
        }
    }

    @Test
    fun `sortPlayers - EXPLOSIVENESS sorts by peak speed descending`() = runTest {
        // Given
        testPlayers.forEach { viewModel.addPlayer(it) }

        // When
        viewModel.setSelectedSortOption(SortOption.EXPLOSIVENESS)

        // Then
        viewModel.leaderBoardUiState.test {
            val actualPlayers = awaitItem().players
            val expectedOrder = testPlayers.sortedByDescending { it.peakSpeed }
            assertEquals(expectedOrder, actualPlayers)
        }
    }

    @Test
    fun `sortPlayers - ENDURANCE sorts by number of laps descending`() = runTest {
        // Given
        testPlayers.forEach { viewModel.addPlayer(it) }

        // When
        viewModel.setSelectedSortOption(SortOption.ENDURANCE)

        // Then
        viewModel.leaderBoardUiState.test {
            val actualPlayers = awaitItem().players
            val expectedOrder = testPlayers.sortedByDescending { it.laps.size }
            assertEquals(expectedOrder, actualPlayers)
        }
    }

    @Test
    fun `playerList emits mapped players from repository`() = runTest {
        viewModel.playerListUiState.test {
            val result = awaitItem()
            assertTrue(result is Result.Success)
            assertEquals(getFakePlayers().content?.toUiPlayerSelection(), result.data)
        }
    }

    @Test
    fun `setSelectedPlayer updates current player`() = runTest {
        // Given
        val selectedPlayer = getFakePlayers().content!!.toUiPlayerSelection()[0]

        // When
        viewModel.setSelectedPlayer(selectedPlayer)

        // Then
        viewModel.currentSelectedPlayer.test {
            assertEquals(selectedPlayer, awaitItem().player)
        }
    }

    @Test
    fun `timer state changes correctly through start-stop cycle`() = runTest {
        // Start timer
        viewModel.toggleTimer()
        assertTrue(viewModel.stopWatchUiState.value.isRunning)

        // Stop timer
        viewModel.toggleTimer()
        assertFalse(viewModel.stopWatchUiState.value.isRunning)
    }

    @Test
    fun `addLap correctly records laps`() = runTest {
        // Given
        viewModel.toggleTimer()

        // When
        repeat(3) { viewModel.addLap() }

        viewModel.toggleTimer()

        // Then
        assertEquals(3, viewModel.stopWatchUiState.value.laps.size)
    }

    @Test
    fun `saveSessionAndRest resets timer state`() = runTest {
        // Given
        viewModel.toggleTimer()
        viewModel.addLap()

        // When
        viewModel.saveSessionAndRest()

        // Then
        viewModel.stopWatchUiState.test {
            val state = awaitItem()
            assertEquals(0, state.timeInMillis)
            assertEquals(emptyList<TimeLap>(), state.laps)
            assertFalse(state.isRunning)
        }
    }

    @Test
    fun `retryPlayerList triggers playerList to loading`() = runTest {
        // When
        viewModel.retryPlayerList()

        // Then
        assertTrue(viewModel.playerListUiState.value is Result.Loading)
    }

    private companion object {
        const val DEFAULT_TIME = 1000L

        fun createTestLaps(count: Int): List<TimeLap> =
            List(count) { index ->
                TimeLap(
                    id = index + 1,
                    lapTime = 100L * (index + 1),
                    totalTime = 100L * (index + 1)
                )
            }
    }
}