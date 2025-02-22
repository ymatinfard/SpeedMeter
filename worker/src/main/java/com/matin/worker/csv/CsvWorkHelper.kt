package com.matin.worker.csv

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo

const val CSV_EXPORT_ONGOING_NOTIFICATION_ID = 1
const val CSV_EXPORT_ONGOING_CHANNEL_ID = "csv_notification_channel"
const val CSV_EXPORT_READY_NOTIFICATION_ID = 2
const val CSV_EXPORT_READY_CHANNEL_ID = "csv_notification_ready_channel"

fun Context.csvExportForegroundInfo(): ForegroundInfo {
    return ForegroundInfo(
        CSV_EXPORT_ONGOING_NOTIFICATION_ID,
        createNotification(
            channelId = CSV_EXPORT_ONGOING_CHANNEL_ID,
            channelName = "CSV Export",
            importance = NotificationManager.IMPORTANCE_LOW,
            title = "SpeedMeter",
            text = "CSV File is creating...",
            ongoing = true
        )
    )
}

fun Context.createCsvExportReadyNotification() {
    val notification = createNotification(
        channelId = CSV_EXPORT_READY_CHANNEL_ID,
        channelName = "CSV WorkManager Notifications",
        importance = NotificationManager.IMPORTANCE_HIGH,
        title = "SpeedMeter",
        text = "CSV File is ready",
        autoCancel = true
    )
    getNotificationManager().notify(CSV_EXPORT_READY_NOTIFICATION_ID, notification)
}

private fun Context.createNotification(
    channelId: String,
    channelName: String,
    importance: Int,
    title: String,
    text: String,
    ongoing: Boolean = false,
    autoCancel: Boolean = false
): Notification {
    createNotificationChannel(channelId, channelName, importance)

    return NotificationCompat.Builder(this, channelId)
        .setContentTitle(title)
        .setContentText(text)
        .setSmallIcon(com.matin.speedmeter.core.common.R.drawable.ic_speed_meter)
        .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
        .setOngoing(ongoing)
        .setAutoCancel(autoCancel)
        .build()
}

private fun Context.createNotificationChannel(channelId: String, channelName: String, importance: Int) {
    val notificationManager = getNotificationManager()
    val existingChannel = notificationManager.getNotificationChannel(channelId)
    if (existingChannel == null) {
        val channel = NotificationChannel(channelId, channelName, importance)
        notificationManager.createNotificationChannel(channel)
    }
}

private fun Context.getNotificationManager(): NotificationManager {
    return getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}
