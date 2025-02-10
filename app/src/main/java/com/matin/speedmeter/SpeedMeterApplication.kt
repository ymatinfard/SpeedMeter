package com.matin.speedmeter

import android.app.Application
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.matin.sync.Sync

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SpeedMeterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupDataSync()
    }

    private fun setupDataSync() {
        val workManager = WorkManager.getInstance(this)
        val workInfo = workManager.getWorkInfosForUniqueWork(Sync.SYNC_WORK_NAME).get()
        val isWorkRunning =
            workInfo.any { it.state == WorkInfo.State.ENQUEUED || it.state == WorkInfo.State.RUNNING }
        if (!isWorkRunning) {
            Sync.init(this)
        }
    }
}