package com.matin.feature.stopwatch.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.matin.feature.stopwatch.model.LeaderBoardUiState
import com.matin.feature.stopwatch.model.UiLeaderBoardPlayer

class LeaderBoardPreviewParameterProvider : PreviewParameterProvider<LeaderBoardUiState> {
    private val samplePlayers = listOf(
        UiLeaderBoardPlayer(id = 0, "Alice", 120f, emptyList(), "https://example.com", 30f),
        UiLeaderBoardPlayer(id = 1, "Bob", 110f, emptyList(), "https://example.com", 30f),
        UiLeaderBoardPlayer(
            id = 2,
            "Charlie",
            130f,
            emptyList(),
            "https://example.com",
            30f
        ),
        UiLeaderBoardPlayer(id = 3, "Diana", 100f, emptyList(), "https://example.com", 30f)
    )

    override val values: Sequence<LeaderBoardUiState>
        get() = sequenceOf(
            LeaderBoardUiState(players = samplePlayers)
        )
}