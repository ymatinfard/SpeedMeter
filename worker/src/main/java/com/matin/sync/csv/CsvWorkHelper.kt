package com.matin.sync.csv

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
        createCsvExportOngoingNotification()
    )
}

fun Context.createCsvExportOngoingNotification(): Notification {
    val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Ensure the notification channel exists (Android 8+)
    val channel = NotificationChannel(
        CSV_EXPORT_ONGOING_CHANNEL_ID,
        "CSV Export",
        NotificationManager.IMPORTANCE_LOW
    )
    notificationManager.createNotificationChannel(channel)
    val notification = NotificationCompat.Builder(applicationContext, CSV_EXPORT_ONGOING_CHANNEL_ID)
        .setContentTitle("SpeedMeter")
        .setContentText("CSV File is creating...")
        .setSmallIcon(com.matin.speedmeter.core.common.R.drawable.ic_speed_meter)
        .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE) // Required for foreground services
        .setOngoing(true)
        .setAutoCancel(true)
        .build()

    return notification
}

fun Context.createCsvExportReadyNotification() {
    val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val channel = NotificationChannel(
        CSV_EXPORT_READY_CHANNEL_ID,
        "CSV WorkManager Notifications",
        NotificationManager.IMPORTANCE_HIGH
    )
    notificationManager.createNotificationChannel(channel)

    val notification = NotificationCompat.Builder(applicationContext, CSV_EXPORT_READY_CHANNEL_ID)
        .setContentTitle("SpeedMeter")
        .setContentText("CSV File is ready")
        .setSmallIcon(com.matin.speedmeter.core.common.R.drawable.ic_speed_meter)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(CSV_EXPORT_READY_NOTIFICATION_ID, notification)
}
