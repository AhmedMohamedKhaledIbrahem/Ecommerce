package com.example.ecommerce.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager

interface INotification {
   val  notificationManager: NotificationManager
    fun notificationChannel(): NotificationChannel
    fun showNotification(message: String)
    fun notificationBuilder(message: String):Notification
}