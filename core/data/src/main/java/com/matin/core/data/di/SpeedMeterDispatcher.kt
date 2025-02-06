package com.matin.core.data.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val speedMeterDispatchers: SpeedMeterDispatcher)

enum class SpeedMeterDispatcher {
    Default,
    IO,
}
