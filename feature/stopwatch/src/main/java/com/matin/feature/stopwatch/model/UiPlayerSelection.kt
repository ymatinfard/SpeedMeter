package com.matin.feature.stopwatch.model

import com.matin.model.Players

data class UiPlayerSelection(
    val id: String,
    val fullName: String,
    val imageUrl: String,
)

fun Players.toUiPlayerSelection(): List<UiPlayerSelection> {
    return players.map {
        UiPlayerSelection(id = it.id, it.name.fullName, it.picture.thumbnail)
    }
}
