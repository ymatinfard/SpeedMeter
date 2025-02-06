package com.matin.core.data.di

import com.matin.core.data.SpeedMeterRepositoryImpl
import com.matin.core.data.SpeedMeterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun provideRepository(repositoryImpl: SpeedMeterRepositoryImpl): SpeedMeterRepository
}