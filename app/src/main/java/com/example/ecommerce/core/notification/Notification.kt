package com.example.ecommerce.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.CHANNEL_ID
import com.example.ecommerce.core.constants.NOTIFICATION_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Notification @Inject constructor(
    @ApplicationContext private val context: Context
) : INotification {
    override val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager

    override fun notificationChannel(): NotificationChannel {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        return NotificationChannel(
            CHANNEL_ID,
            NOTIFICATION_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableLights(true)
            lightColor = 0xFFFFFFFF.toInt()
            setSound(soundUri, audioAttributes)
        }
    }

    override fun showNotification(message: String) {
        val channel = notificationChannel()
        notificationManager.createNotificationChannel(channel)
        val notification = notificationBuilder(message = message)
        notificationManager.notify(1, notification)

    }

    override fun notificationBuilder(message: String): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("Order Status")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
    }
}