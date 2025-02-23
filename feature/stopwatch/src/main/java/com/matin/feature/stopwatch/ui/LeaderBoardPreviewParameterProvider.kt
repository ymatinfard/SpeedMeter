package com.matin.feature.stopwatch.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.matin.feature.stopwatch.model.LeaderBoarUiData
import com.matin.feature.stopwatch.model.LeaderBoardUiState
import com.matin.feature.stopwatch.model.UiLeaderBoardPlayer

class LeaderBoardPreviewParameterProvider : PreviewParameterProvider<LeaderBoardUiState> {
    private val samplePlayers = listOf(
        UiLeaderBoardPlayer(id = "100", sessionId = "100-000000", "Alice", 120f, emptyList(), "https://example.com", 30f),
        UiLeaderBoardPlayer(id = "101", sessionId = "101-222222", "Bob", 110f, emptyList(), "https://example.com", 30f),
        UiLeaderBoardPlayer(id = "102", sessionId = "102-333333", "Charlie", 130f, emptyList(), "https://example.com", 30f),
        UiLeaderBoardPlayer(id = "103", sessionId = "103-444444", "Diana", 100f, emptyList(), "https://example.com", 30f)
    )

    override val values: Sequence<LeaderBoardUiState>
        get() = sequenceOf(
            LeaderBoardUiState.Success(data = LeaderBoarUiData(players = samplePlayers))
        )
}