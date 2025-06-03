package com.example.ecommerce.features.notification.domain.repository

import com.example.ecommerce.features.notification.domain.entity.NotificationRequestEntity
import com.example.ecommerce.features.notification.domain.entity.NotificationResponseEntity

interface NotificationRepository {
    suspend fun saveToken(notificationRequestParams: NotificationRequestEntity): NotificationResponseEntity

}