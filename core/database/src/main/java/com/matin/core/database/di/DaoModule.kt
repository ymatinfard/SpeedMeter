package com.matin.core.database.di

import com.matin.core.database.SpeedMeterDatabase
import com.matin.core.database.dao.StopWatchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    fun provideStopWatchDao(database: SpeedMeterDatabase): StopWatchDao = database.stopWatchDao()
}