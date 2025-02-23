package com.matin.core.database

import androidx.room.Embedded
import androidx.room.Relation

data class PlayerWithSessionAndLaps(
    @Embedded val player: PlayerEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "playerId",
        entity = SessionEntity::class
    )
    var sessionWithLaps: List<SessionWithLaps>
) {
    constructor() : this(
        player = PlayerEntity(id = "0", fullName = "", imageUrl = ""),
        sessionWithLaps = mutableListOf()
    )
}

