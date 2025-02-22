package com.matin.worker.csv

import android.annotation.SuppressLint
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.matin.core.data.SpeedMeterRepository
import com.matin.core.data.di.Dispatcher
import com.matin.core.data.di.SpeedMeterDispatcher
import com.matin.worker.DelegatingWorker
import com.matin.worker.delegatedData
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltWorker
class CSVExportWorker
@AssistedInject
constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val repository: SpeedMeterRepository,
    @Dispatcher(SpeedMeterDispatcher.IO) val ioDispatcher: CoroutineDispatcher,
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return appContext.csvExportForegroundInfo()
    }

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result =
        withContext(ioDispatcher) {
            try {
                setForegroundAsync(getForegroundInfo())
                repository.createCSVFile()
                appContext.createCsvExportReadyNotification()
                Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                Result.retry()
            }
        }

    companion object {
        fun exportCSVFileWorker() = OneTimeWorkRequestBuilder<DelegatingWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInputData(CSVExportWorker::class.delegatedData())
            .build()
    }
}
