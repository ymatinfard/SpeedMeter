package com.matin.sync

import android.annotation.SuppressLint
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.matin.core.data.SpeedMeterRepository
import com.matin.core.data.di.Dispatcher
import com.matin.core.data.di.SpeedMeterDispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncWorker
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
                repository.sync()
                Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                Result.retry()
            }
        }

    companion object {
        private const val REPEAT_INTERVAL = 30L
        private const val FLEX_TIME_INTERVAL = 10L

        fun periodicSyncWork() = PeriodicWorkRequestBuilder<DelegatingWorker>(
            REPEAT_INTERVAL,
            TimeUnit.MINUTES,
            FLEX_TIME_INTERVAL,
            TimeUnit.MINUTES
        ).setConstraints(SyncConstraints)
            .setInputData(SyncWorker::class.delegatedData())
            .build()
    }
}
