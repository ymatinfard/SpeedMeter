package com.matin.speedmeter

import android.app.Application
import com.matin.sync.Sync
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SpeedMeterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Sync.init(this)
    }
}
