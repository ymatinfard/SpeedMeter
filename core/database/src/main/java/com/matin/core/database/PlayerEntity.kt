package com.matin.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")
data class PlayerEntity(
    @PrimaryKey val id: Int,
    val fullName: String,
    val imageUrl: String,
)
