package com.example.ecommerce.core.service

import com.example.ecommerce.core.manager.prdouct.ProductHandler
import com.example.ecommerce.core.notification.INotification
import com.example.ecommerce.features.notification.domain.repository.NotificationManagerRepository
import com.example.ecommerce.features.product.domain.repository.ProductRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FCMHandlerEntryPoint {
    fun getNotification(): INotification
    fun getNotificationManagerRepository(): NotificationManagerRepository
    fun getProductRepository(): ProductRepository
    fun getProductHandler(): ProductHandler
}