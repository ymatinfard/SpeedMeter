package com.matin.sync.csv

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo

const val CSV_EXPORT_NOTIFICATION_ID = 1
const val CSV_EXPORT_CHANNEL_ID = "csv_notification_channel"

fun Context.csvExportForegroundInfo(): ForegroundInfo {
    return ForegroundInfo(
        CSV_EXPORT_NOTIFICATION_ID,
        createCsvExportNotification()
    )
}

fun Context.createCsvExportNotification(): Notification {
    val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val channel = NotificationChannel(
        CSV_EXPORT_CHANNEL_ID,
        "CSV WorkManager Notifications",
        NotificationManager.IMPORTANCE_HIGH
    )
    notificationManager.createNotificationChannel(channel)

    val notification = NotificationCompat.Builder(applicationContext, CSV_EXPORT_CHANNEL_ID)
        .setContentTitle("SpeedMeter")
        .setContentText("CSV File Created Successfully")
        .setSmallIcon(com.matin.speedmeter.core.common.R.drawable.ic_speed_meter)
        .setAutoCancel(true)
        .build()

    return notification
}
