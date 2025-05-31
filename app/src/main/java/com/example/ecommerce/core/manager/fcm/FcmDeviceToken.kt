package com.example.ecommerce.core.manager.fcm

interface FcmDeviceToken {
    suspend fun addFcmTokenDevice(token: String)
    fun getFcmTokenDevice():String?
    suspend fun deleteFcmTokenDevice()
}