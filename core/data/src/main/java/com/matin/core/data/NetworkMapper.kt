package com.matin.core.data

import android.net.Network
import com.matin.core.network.model.NetworkName
import com.matin.core.network.model.NetworkPicture
import com.matin.core.network.model.NetworkPlayer
import com.matin.core.network.model.NetworkPlayers
import com.matin.model.Name
import com.matin.model.Picture
import com.matin.model.Player
import com.matin.model.Players
import com.matin.model.orEmpty

fun NetworkPlayers.toDomain() = Players(
    players = this.players.mapOrEmpty { it.toDomain() }
)

fun NetworkPlayer.toDomain() = Player(
    name = name?.toDomain().orEmpty() ,
    picture = picture?.toDomain().orEmpty()
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

private fun <T, R> Collection<T>?.mapOrEmpty(transform: (T) -> R): List<R> {
    return this?.mapNotNull { item ->
        runCatching { transform(item) }.getOrNull()
    } ?: emptyList()
}
