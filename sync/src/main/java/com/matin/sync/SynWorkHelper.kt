package com.matin.sync

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import com.matin.speedmeter.sync.R

const val SYNC_NOTIFICATION_ID = 0
const val SYNC_NOTIFICATION_CHANNEL_ID = "sync_notification_channel"

val SyncConstraints
    get() =
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

fun Context.syncForegroundInfo(): ForegroundInfo {
    return ForegroundInfo(
        SYNC_NOTIFICATION_ID,
        syncWorkNotification(),
    )
}

fun Context.syncWorkNotification(): Notification {
    val channel =
        NotificationChannel(
            SYNC_NOTIFICATION_CHANNEL_ID,
            "Sync",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "Background task for HappyStore"
        }

    val notificationManager: NotificationManager? =
        getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

    notificationManager?.createNotificationChannel(channel)

    return NotificationCompat.Builder(this, SYNC_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_speed_meter)
        .setContentTitle("SpeedMeter").setPriority(NotificationCompat.PRIORITY_DEFAULT).build()
}
