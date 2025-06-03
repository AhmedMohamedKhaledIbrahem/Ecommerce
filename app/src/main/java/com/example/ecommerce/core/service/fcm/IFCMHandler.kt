package com.example.ecommerce.core.service.fcm

import com.google.firebase.messaging.RemoteMessage

interface IFCMHandler {
    fun processInternalNotification(message: RemoteMessage)
    fun processExternalNotification(message: RemoteMessage)
}