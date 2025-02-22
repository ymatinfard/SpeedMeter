package com.matin.core.database

import androidx.room.Embedded
import androidx.room.Relation


data class SessionWithLaps(
    @Embedded val session: SessionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "sessionId",
        entity = TimeLapEntity::class
    )
    val laps: List<TimeLapEntity>
)
