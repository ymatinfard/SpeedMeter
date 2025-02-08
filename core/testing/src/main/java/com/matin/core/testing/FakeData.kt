package com.matin.core.testing

import com.matin.model.Name
import com.matin.model.Picture
import com.matin.model.Player
import com.matin.model.Players

fun getFakePlayers() = Players(
    players = listOf(
        Player(
            name = Name("Mr", "Yousef", "Matin"),
            Picture(
                large = "large.jpeg",
                medium = "medium.jpeg",
                thumbnail = "thumbnail.jpeg"
            )
        ),
        Player(
            name = Name("Mr", "Martin", "Fowler"),
            Picture(
                large = "large.jpeg",
                medium = "medium.jpeg",
                thumbnail = "thumbnail.jpeg"
            )
        ),
    )
)