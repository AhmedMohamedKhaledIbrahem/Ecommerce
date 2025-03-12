package com.example.ecommerce.features.notification.module

import com.example.ecommerce.core.database.data.dao.orders.OrderTagDao
import com.example.ecommerce.core.manager.fcm.FcmDeviceToken
import com.example.ecommerce.features.notification.data.source.local.NotificationLocalDataSource
import com.example.ecommerce.features.notification.data.source.local.NotificationLocalDataSourceImp
import com.example.ecommerce.features.notification.data.source.remote.FcmApi
import com.example.ecommerce.features.notification.data.source.remote.NotificationRemoteDataSource
import com.example.ecommerce.features.notification.data.source.remote.NotificationRemoteDataSourceImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideNotificationLocalDataSource(
        dao: OrderTagDao,
        fcmDeviceToken: FcmDeviceToken
    ): NotificationLocalDataSource {
        return NotificationLocalDataSourceImp(dao = dao, fcmDeviceToken = fcmDeviceToken)
    }

    @Provides
    @Singleton
    fun provideNotificationRemoteDataSource(
        api: FcmApi
    ): NotificationRemoteDataSource {
        return NotificationRemoteDataSourceImp(
            api = api
        )
    }


}