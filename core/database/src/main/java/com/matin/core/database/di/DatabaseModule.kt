package com.matin.core.database.di

import android.content.Context
import androidx.room.Room
import com.matin.core.database.SpeedMeterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): SpeedMeterDatabase {
        return Room.databaseBuilder(
            context, SpeedMeterDatabase::class.java, "speed_meter_database"
        ).build()
    }
}