package com.example.ecommerce.features.notification.domain.repository

interface NotificationRepository {
    suspend fun saveToken(token: String)

}