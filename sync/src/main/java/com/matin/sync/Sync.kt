package com.matin.sync

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager

internal const val SYNC_WORK_NAME = "sync_work_name"

object Sync {
    fun init(context: Context) {
        WorkManager.getInstance(context).apply {
            enqueueUniquePeriodicWork(SYNC_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, SyncWorker.periodicSyncWork())
        }
    }
}
