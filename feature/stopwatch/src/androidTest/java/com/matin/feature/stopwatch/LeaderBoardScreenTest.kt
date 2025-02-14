package com.matin.feature.stopwatch

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.matin.core.testing.GrantPostNotificationsPermissionRule
import com.matin.feature.stopwatch.model.LeaderBoardUiState
import com.matin.feature.stopwatch.model.TimeLap
import com.matin.feature.stopwatch.model.UiLeaderBoardPlayer
import com.matin.speedmeter.feature.stopwatch.R
import org.junit.Rule
import org.junit.Test

class LeaderBoardScreenTest {

    @get:Rule(order = 0)
    val permissionRule: GrantPostNotificationsPermissionRule =
        GrantPostNotificationsPermissionRule()

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun startNewSessionButton_whenScreenLoaded_exists() {
        composeTestRule.setContent {
            Box {
                LeaderBoardScreenContent(
                    state = LeaderBoardUiState()
                )
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.resources.getString(R.string.feature_stopwatch_start_new_session))
            .assertExists()
    }

    @Test
    fun leaderboardEmptyState_whenScreenLoaded_exists() {
        composeTestRule.setContent {
            Box {
                LeaderBoardScreenContent(
                    state = LeaderBoardUiState()
                )
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.resources.getString(R.string.feature_stopwatch_leaderboard_is_currently_empty))
            .assertExists()
    }

    @Test
    fun leaderBoardListItem_whenScreenLoaded_exits() {
        composeTestRule.setContent {
            Box {
                LeaderBoardScreenContent(
                    state = LeaderBoardUiState(
                        players = listOf(
                            UiLeaderBoardPlayer(
                                1, "Mr Yousef Matinfard", peakSpeed = 12.2f, laps = listOf(
                                    TimeLap(1, 12000, 23000)
                                ), imageUrl = "", distance = 34f
                            )
                        )
                    )
                )
            }
        }

        composeTestRule.onNodeWithText("Mr Yousef Matinfard").assertExists()
    }

    @Test
    fun leaderBoardListItem_whenScreenLoaded_hasPeakSpeedAndLaps() {
        composeTestRule.setContent {
            Box {
                LeaderBoardScreenContent(
                    state = LeaderBoardUiState(
                        players = listOf(
                            UiLeaderBoardPlayer(
                                1, "Mr Yousef Matinfard", peakSpeed = 12.22f, laps = listOf(
                                    TimeLap(1, 12000, 23000)
                                ), imageUrl = "", distance = 34f
                            )
                        )
                    )
                )
            }
        }

        composeTestRule.onNodeWithText(
            composeTestRule.activity.resources.getString(
                R.string.feature_stopwatch_peak_speed_m_s,
                12.22f
            )
        )
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.resources.getString(
                R.string.feature_stopwatch_laps,
                1
            )
        )
            .assertIsDisplayed()
    }

    @Test
    fun leaderBoardTopBar_fileExportButtonIsDisplayed() {
        composeTestRule.setContent {
            Box {
                LeaderBoardScreenContent(
                    state = LeaderBoardUiState()
                )
            }
        }

        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.resources.getString(R.string.feature_stopwatch_export_file))
            .assertIsDisplayed()
    }
}