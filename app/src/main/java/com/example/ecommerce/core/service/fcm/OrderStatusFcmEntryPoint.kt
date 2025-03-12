package com.example.ecommerce.core.service.fcm

import com.example.ecommerce.core.notification.INotification
import com.example.ecommerce.features.notification.domain.repository.NotificationManagerRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface OrderStatusFcmEntryPoint {
    fun getNotification():INotification
    fun getNotificationManagerRepository(): NotificationManagerRepository
}