package com.matin.core.data

import com.matin.core.network.model.NetworkName
import com.matin.core.network.model.NetworkPicture
import com.matin.core.network.model.NetworkPlayer
import com.matin.core.network.model.NetworkPlayers
import com.matin.model.Name
import com.matin.model.Picture
import com.matin.model.Player
import com.matin.model.Players

val fakeNetworkPlayers = NetworkPlayers(
    listOf(
        NetworkPlayer(
            NetworkName("Mr", "Yousef", "Matinfard"),
            NetworkPicture("large.jpg", "medium.jpg", "thumbnail.jpg")
        ),
        NetworkPlayer(
            NetworkName("Mr", "Jake", "Warton"),
            NetworkPicture("large.jpg", "medium.jpg", "thumbnail.jpg")
        )
    )
)

val fakeDomainPlayers = Players(
    listOf(
        Player(
            name = Name("Mr", "Yousef", "Matinfard"),
            picture = Picture("large.jpg", "medium.jpg", "thumbnail.jpg")
        ),
        Player(
            name = Name("Mr", "Jake", "Warton"),
            picture = Picture("large.jpg", "medium.jpg", "thumbnail.jpg")
        )
    )
)