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
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val playerId: String, // Foreign key to PlayerEntity
    val sessionId: String, // Foreign key to SessionEntity
    val lapNumber: Int,
    val lapTime: Long,
    val totalTime: Long,
)