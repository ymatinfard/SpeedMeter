package com.matin.feature.stopwatch.model

import com.matin.model.Players

data class UiPlayerSelection(
    val fullName: String,
    val imageUrl: String,
)

fun Players.toUiPlayerSelection(): List<UiPlayerSelection> {
    return players.map {
        UiPlayerSelection(it.name.fullName, it.picture.thumbnail)
    }
}
