package com.matin.worker.sync

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager


object Sync {
    fun init(context: Context) {
        WorkManager.getInstance(context).apply {
            enqueueUniquePeriodicWork(
                SYNC_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP,
                SyncWorker.periodicSyncWork()
            )
        }
    }

    const val SYNC_WORK_NAME = "sync_work_name"
}
