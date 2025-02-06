package com.matin.core.data

import com.matin.core.network.model.NetworkName
import com.matin.core.network.model.NetworkPicture
import com.matin.core.network.model.NetworkPlayer
import com.matin.core.network.model.NetworkPlayers
import com.matin.model.Name
import com.matin.model.Picture
import com.matin.model.Player
import com.matin.model.Players

fun NetworkPlayers.toDomain() = Players(
    players = players?.map { it.toDomain() } ?: emptyList()
)

fun NetworkPlayer.toDomain() = Player(
    name = name?.toDomain() ?: Name.empty(),
    picture = picture?.toDomain() ?: Picture.empty(),
)

fun NetworkName.toDomain() = Name(
    title = title.orEmpty(),
    first = first.orEmpty(),
    last = last.orEmpty()
)

fun NetworkPicture.toDomain() = Picture(
    large = large.orEmpty(),
    medium = medium.orEmpty(),
    thumbnail = thumbnail.orEmpty()
)
