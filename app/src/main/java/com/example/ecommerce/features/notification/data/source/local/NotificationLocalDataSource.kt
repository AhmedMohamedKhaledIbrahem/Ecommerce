package com.example.ecommerce.features.notification.data.source.local

interface NotificationLocalDataSource {
    suspend fun updateOrderStatus(orderId: Int, status: String)
    suspend fun addFcmTokenDevice(token: String)

}