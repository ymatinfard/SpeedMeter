package com.matin.sync.csv

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CSVExporter @Inject constructor(@ApplicationContext private val context: Context) {
    fun initCSVFileExporter() {
        WorkManager.getInstance(context).apply {
            enqueueUniqueWork(
                CSV_EXPORT_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                CSVExportWorker.exportCSVFileWorker()
            )
        }
    }

    companion object {
        const val CSV_EXPORT_WORK_NAME = "csv_export_work_name"
    }
}
