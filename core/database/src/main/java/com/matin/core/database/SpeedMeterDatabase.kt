package com.matin.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.matin.core.database.dao.StopWatchDao

@Database(
    entities = [PlayerEntity::class, SessionEntity::class, TimeLapEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SpeedMeterDatabase : RoomDatabase() {
    abstract fun stopWatchDao(): StopWatchDao
}