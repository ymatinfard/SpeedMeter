package com.matin.core.testing

import com.matin.core.common.Data
import com.matin.model.Name
import com.matin.model.Picture
import com.matin.model.Player
import com.matin.model.Players

fun getFakePlayers() = Data(
    content = Players(
        players = listOf(
            Player(
                id = "100",
                name = Name("Mr", "Yousef", "Matin"),
                picture = Picture(
                    large = "large.jpeg",
                    medium = "medium.jpeg",
                    thumbnail = "thumbnail.jpeg"
                )
            ),
            Player(
                id = "101",
                name = Name("Mr", "Martin", "Fowler"),
                picture = Picture(
                    large = "large.jpeg",
                    medium = "medium.jpeg",
                    thumbnail = "thumbnail.jpeg"
                )
            ),
        )
    )
)