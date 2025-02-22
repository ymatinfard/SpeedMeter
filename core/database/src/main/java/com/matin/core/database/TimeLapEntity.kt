package com.matin.core.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "time_lap",
    foreignKeys = [
        ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = ["id"],
            childColumns = ["playerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index("playerId"), Index("sessionId")]
)
data class TimeLapEntity(
    @PrimaryKey val id: Int = 0,
    val playerId: Int, // Foreign key to PlayerEntity
    val sessionId: Int, // Foreign key to SessionEntity
    val lapNumber: Int,
    val lapTime: Long,
    val totalTime: Long,
)