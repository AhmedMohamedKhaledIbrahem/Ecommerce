package com.example.ecommerce.core.service

import com.google.firebase.messaging.RemoteMessage

interface IFCMHandler {
    fun processInternalNotification(message: RemoteMessage)
    fun processExternalNotification(message: RemoteMessage)
}