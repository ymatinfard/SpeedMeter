package com.matin.feature.stopwatch.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.matin.core.common.TimeFormatter.formatTime
import com.matin.feature.stopwatch.managers.StopwatchTimer
import com.matin.speedmeter.feature.stopwatch.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StopwatchService : Service() {

    @Inject
    lateinit var stopwatchTimer: StopwatchTimer

    private val scope = CoroutineScope(Dispatchers.Default)

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            TimerState.Start.name,
            TimerState.Pause.name -> stopwatchTimer.toggleTimer()
        }

        startForeground(
            NOTIFICATION_ID,
            createNotification(
                SystemClock.elapsedRealtime(),
                getNotificationButtonText(stopwatchTimer.isRunning())
            )
        )
        startStopwatch()

        return START_STICKY
    }

    private fun getNotificationButtonText(isTimerRunning: Boolean): TimerState {
        return if (isTimerRunning) TimerState.Pause else TimerState.Start
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun startStopwatch() {
        scope.launch {
            stopwatchTimer.elapsedTime.collect { elapsedTime ->
                val notification = createNotification(
                    elapsedTime, getNotificationButtonText(stopwatchTimer.isRunning())
                )
                with(NotificationManagerCompat.from(this@StopwatchService)) {
                    notify(NOTIFICATION_ID, notification)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    fun createNotification(elapsedTime: Long, state: TimerState): Notification {
        val startIntent = Intent(this, StopwatchService::class.java).apply {
            action = TimerState.Start.name
        }

        val pauseIntent = Intent(this, StopwatchService::class.java).apply {
            action = TimerState.Pause.name
        }

        val startPendingIntent =
            PendingIntent.getService(this, 0, startIntent, PendingIntent.FLAG_IMMUTABLE)
        val pausePendingIntent =
            PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE)

        val pendingIntent: PendingIntent
        val actionText: String
        val icon: Int

        when (state) {
            TimerState.Start -> {
                pendingIntent = startPendingIntent
                actionText = TimerState.Start.name
                icon = R.drawable.ic_play
            }

            TimerState.Pause -> {
                pendingIntent = pausePendingIntent
                actionText = TimerState.Pause.name
                icon = R.drawable.ic_pause
            }
        }

        val timeFormatted = formatTime(elapsedTime)

        val notificationChannelId = "stopwatch_channel"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            notificationChannelId,
            "Stopwatch",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Shows stopwatch time while app is in background"
        }
        notificationManager.createNotificationChannel(channel)

        return NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Stopwatch Running")
            .setContentText("Elapsed time: $timeFormatted")
            .setSmallIcon(com.matin.speedmeter.core.common.R.drawable.ic_speed_meter)
            .addAction(icon, actionText, pendingIntent)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
    }

    enum class TimerState {
        Start,
        Pause,
    }

    companion object {
        const val NOTIFICATION_ID = 1
    }
}