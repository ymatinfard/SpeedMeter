package com.matin.core.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "session",
    foreignKeys = [
        ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = ["id"],
            childColumns = ["playerId"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index("playerId")]
)
data class SessionEntity(
    @PrimaryKey val id: String,
    val playerId: String,
    val distance: Float,
)
