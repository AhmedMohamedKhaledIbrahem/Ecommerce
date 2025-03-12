package com.example.ecommerce.core.servicelocator

import android.content.Context
import android.content.SharedPreferences
import com.example.ecommerce.core.database.data.dao.orders.OrderTagDao
import com.example.ecommerce.core.manager.fcm.FcmDeviceToken
import com.example.ecommerce.core.manager.fcm.FcmDeviceTokenImp
import com.example.ecommerce.core.notification.INotification
import com.example.ecommerce.core.notification.Notification
import com.example.ecommerce.features.notification.data.repository.NotificationManagerRepositoryImp
import com.example.ecommerce.features.notification.data.source.local.NotificationLocalDataSource
import com.example.ecommerce.features.notification.data.source.local.NotificationLocalDataSourceImp
import com.example.ecommerce.features.notification.data.source.remote.FcmApi
import com.example.ecommerce.features.notification.data.source.remote.NotificationRemoteDataSource
import com.example.ecommerce.features.notification.data.source.remote.NotificationRemoteDataSourceImp
import com.example.ecommerce.features.notification.domain.repository.NotificationManagerRepository

object ServiceLocator {


    private var _fcmDeviceToken: FcmDeviceToken? = null
    val fcmDeviceToken: FcmDeviceToken
        get() = _fcmDeviceToken
            ?: throw UninitializedPropertyAccessException("SocketManager not initialized")


    private var _notification: INotification? = null
    val notification: INotification
        get() = _notification
            ?: throw UninitializedPropertyAccessException("Notification not initialized")


    private var _notificationRemoteDataSource: NotificationRemoteDataSource? = null
    val notificationRemoteDataSource: NotificationRemoteDataSource
        get() = _notificationRemoteDataSource
            ?: throw UninitializedPropertyAccessException("NotificationRemoteDataSource not initialized")

    private var _notificationLocalDataSource: NotificationLocalDataSource? = null
    val notificationLocalDataSource: NotificationLocalDataSource
        get() = _notificationLocalDataSource
            ?: throw UninitializedPropertyAccessException("NotificationLocalDataSource not initialized")


    private var _notificationManagerRepository: NotificationManagerRepository? = null
    val notificationManagerRepository: NotificationManagerRepository
        get() = _notificationManagerRepository
            ?: throw UninitializedPropertyAccessException("NotificationRepository not initialized")


    fun provideNotificationLocalDataSource(
        dao: OrderTagDao,
        fcmDeviceToken: FcmDeviceToken
    ): NotificationLocalDataSource? {
        _notificationLocalDataSource = NotificationLocalDataSourceImp(
            dao = dao,
            fcmDeviceToken = fcmDeviceToken
        )
        return _notificationLocalDataSource
    }

    fun provideNotificationManagerRepository(
        localDataSource: NotificationLocalDataSource
    ): NotificationManagerRepository? {
        _notificationManagerRepository = NotificationManagerRepositoryImp(
            localDataSource = localDataSource
        )
        return _notificationManagerRepository
    }

    fun provideFcmDeviceToken(preferences: SharedPreferences): FcmDeviceToken? {
        _fcmDeviceToken = FcmDeviceTokenImp(preferences)
        return _fcmDeviceToken
    }


    fun provideNotificationRemoteDataSource(
        api: FcmApi
    ): NotificationRemoteDataSource? {
        _notificationRemoteDataSource = NotificationRemoteDataSourceImp(
            api = api
        )
        return _notificationRemoteDataSource
    }

    fun provideNotification(context: Context): INotification? {
        _notification = Notification(context = context)
        return _notification
    }

    fun isInitialized(): Boolean {
        return _fcmDeviceToken != null &&
                _notification != null &&
                _notificationManagerRepository != null &&
                _notificationLocalDataSource != null


    }
    fun isInitialized2(): Boolean {
        return _notificationManagerRepository != null &&
                _notificationLocalDataSource != null


    }



}