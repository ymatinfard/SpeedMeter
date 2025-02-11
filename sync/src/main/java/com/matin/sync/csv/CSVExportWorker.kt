package com.matin.sync.csv

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.matin.core.data.SpeedMeterRepository
import com.matin.core.data.di.Dispatcher
import com.matin.core.data.di.SpeedMeterDispatcher
import com.matin.speedmeter.sync.R
import com.matin.sync.DelegatingWorker
import com.matin.sync.delegatedData
import com.matin.sync.syncForegroundInfo
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
        return appContext.syncForegroundInfo()
    }

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result =
        withContext(ioDispatcher) {
            try {
                repository.createCSVFile()
                showNotification("CSV File Created", "CSV File Created Successfully")
                Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                Result.retry()
            }
        }

    private fun showNotification(title: String, message: String) {
        val channelId = "csv_work_manager_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "CSV WorkManager Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(com.matin.speedmeter.core.common.R.drawable.ic_speed_meter)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        fun exportCSVFileWorker() = OneTimeWorkRequestBuilder<DelegatingWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInputData(CSVExportWorker::class.delegatedData())
            .build()
    }
}
