package com.matin.core.data.di

import com.matin.core.data.SpeedMeterRepository
import com.matin.core.data.repository.SpeedMeterRepositoryImpl
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