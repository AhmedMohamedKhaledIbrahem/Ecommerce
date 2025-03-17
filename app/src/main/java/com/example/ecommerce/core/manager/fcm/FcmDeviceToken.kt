package com.example.ecommerce.core.manager.fcm

interface FcmDeviceToken {
    suspend fun addFcmTokenDevice(token: String)
    suspend fun getFcmTokenDevice():String?

    suspend fun deleteFcmTokenDevice()
}