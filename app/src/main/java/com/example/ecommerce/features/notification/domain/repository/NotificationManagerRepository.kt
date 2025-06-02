package com.example.ecommerce.features.notification.domain.repository

interface NotificationManagerRepository {
    suspend fun addFcmTokenDevice(token: String)
    suspend fun updateOrderStatus(orderId: Int, status: String)
}