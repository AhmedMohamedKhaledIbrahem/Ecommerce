package com.example.ecommerce.core.notification

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.CHANNEL_ID

fun INotification.fcmNotificationBuilder(title:String , body:String , context: Context) :Notification{
    return NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.baseline_notifications_24)
        .setContentTitle(title)
        .setContentText(body)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()
}
fun INotification.showNotification(title:String , body:String , context: Context){
    val channel = notificationChannel()
    notificationManager.createNotificationChannel(channel)
    val notification = fcmNotificationBuilder(title = title , body = body , context = context)
    notificationManager.notify(1, notification)
}
