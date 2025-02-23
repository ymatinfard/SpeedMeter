package com.matin.core.data

import com.matin.model.Name
import com.matin.model.Picture
import com.matin.model.Player
import com.matin.model.Players

object FakeApiData {
    fun getDomainMappedPlayers(): Players = Players(
        players = listOf(
            Player(
                id = "100",
                name = Name(
                    title = "Mr",
                    first = "John",
                    last = "Doe"
                ),
                picture = Picture(
                    large = "https://randomuser.me/api/portraits/men/41.jpg",
                    medium = "https://randomuser.me/api/portraits/med/men/41.jpg",
                    thumbnail = "https://randomuser.me/api/portraits/thumb/men/41.jpg"
                )
            ),
            Player(
                id = "101",
                name = Name(
                    title = "Mrs",
                    first = "Jane",
                    last = "Smith"
                ),
                picture = Picture(
                    large = "https://randomuser.me/api/portraits/men/91.jpg",
                    medium = "https://randomuser.me/api/portraits/med/men/91.jpg",
                    thumbnail = "https://randomuser.me/api/portraits/thumb/men/91.jpg"
                )
            ),
            Player(
                id = "102",
                name = Name(
                    title = "Mr",
                    first = "Bob",
                    last = "Johnson"
                ),
                picture = Picture(
                    large = "https://randomuser.me/api/portraits/men/28.jpg",
                    medium = "https://randomuser.me/api/portraits/med/men/28.jpg",
                    thumbnail = "https://randomuser.me/api/portraits/thumb/men/28.jpg"
                )
            )
        )
    )
}