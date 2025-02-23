package com.matin.feature.stopwatch

import app.cash.turbine.test
import com.matin.core.common.Result
import com.matin.core.common.SortOption
import com.matin.core.data.SpeedMeterRepository
import com.matin.core.testing.MainDispatcherRule
import com.matin.feature.stopwatch.managers.LapManager
import com.matin.feature.stopwatch.managers.LeaderboardManager
import com.matin.feature.stopwatch.managers.PlayerListManager
import com.matin.feature.stopwatch.managers.PlayerSessionManager
import com.matin.feature.stopwatch.managers.StopwatchTimeManager
import com.matin.feature.stopwatch.model.LeaderBoarUiData
import com.matin.feature.stopwatch.model.LeaderBoardUiState
import com.matin.feature.stopwatch.model.UiPlayerSelection
import com.matin.worker.csv.CSVExporter
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StopwatchSharedViewModelTest {


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var repository: SpeedMeterRepository

    @MockK
    private lateinit var csvExporter: CSVExporter

    @MockK
    private lateinit var stopwatchTimeManager: StopwatchTimeManager

    @MockK
    private lateinit var lapManager: LapManager

    @MockK
    private lateinit var leaderboardManager: LeaderboardManager

    @MockK
    private lateinit var playerSessionManager: PlayerSessionManager

    @MockK
    private lateinit var playerListManager: PlayerListManager

    private lateinit var viewModel: StopwatchSharedViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        // Default mock behaviors
        coEvery { repository.observeSessionChanges() } returns flowOf(emptyList())
        coEvery { leaderboardManager.generateLeaderboardState(any()) } returns flowOf(LeaderBoardUiState.Success(LeaderBoarUiData(emptyList(), SortOption.EXPLOSIVENESS)))
        coEvery { playerListManager.fetchPlayerList() } returns flowOf(Result.Success(emptyList()))
        every { stopwatchTimeManager.calculateCurrentTime(any()) } returns 1000L
        every { csvExporter.initCSVFileExporter() } just Runs

        viewModel = StopwatchSharedViewModel(
            repository = repository,
            csvExporter = csvExporter,
            stopwatchTimeManager = stopwatchTimeManager,
            lapManager = lapManager,
            leaderboardManager = leaderboardManager,
            playerSessionManager = playerSessionManager,
            playerListManager = playerListManager
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `updateSortOption updates sort option state`() = runTest {
        // Given
        val newSortOption = SortOption.ENDURANCE

        // When
        viewModel.updateSortOption(newSortOption)

        // Then
        viewModel.sortOption.test {
            assertEquals(newSortOption, awaitItem())
        }
    }

    @Test
    fun `updateSelectedPlayer updates player state correctly`() = runTest {
        // Given
        val testPlayer = UiPlayerSelection(id = "1", fullName = "Test Player", imageUrl = "")
        val testDistance = 100f

        // When
        viewModel.updateSelectedPlayer(testPlayer, testDistance)

        // Then
        viewModel.selectedPlayer.test {
            val result = awaitItem()
            assertEquals(testPlayer, result.player)
            assertEquals(testDistance, result.distance)
        }
    }

    @Test
    fun `exportToCsv calls CSV exporter`() = runTest {
        // When
        viewModel.exportToCsv()

        // Then
        verify { csvExporter.initCSVFileExporter() }
    }

    @Test
    fun `retryLoadingPlayerList triggers player list refresh`() = runTest {
        // Given
        val mockPlayers = listOf(UiPlayerSelection(id = "1", fullName = "Test Player", imageUrl = ""))
        coEvery { playerListManager.fetchPlayerList() } returns flowOf(Result.Success(mockPlayers))

        // When
        viewModel.retryLoadingPlayerList()

        // Then
        viewModel.playerList.test {
            val result = awaitItem()
            assertTrue(result is Result.Success)
            assertEquals(mockPlayers, (result as Result.Success).data)
        }
    }
}