package com.example.ecommerce.features.notification.module

import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.notification.data.repository.NotificationManagerRepositoryImp
import com.example.ecommerce.features.notification.data.repository.NotificationRepositoryImp
import com.example.ecommerce.features.notification.data.source.local.NotificationLocalDataSource
import com.example.ecommerce.features.notification.data.source.remote.NotificationRemoteDataSource
import com.example.ecommerce.features.notification.domain.repository.NotificationManagerRepository
import com.example.ecommerce.features.notification.domain.repository.NotificationRepository
import com.example.ecommerce.features.notification.domain.usecase.addfcmtokendevice.AddFcmTokenDeviceUseCase
import com.example.ecommerce.features.notification.domain.usecase.addfcmtokendevice.IAddFcmTokenDeviceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    @Singleton
    fun provideAddFcmTokenDeviceUseCase(
        repository: NotificationRepository
    ): IAddFcmTokenDeviceUseCase {
        return AddFcmTokenDeviceUseCase(repository = repository)
    }


    @Provides
    @Singleton
    fun provideNotificationRepository(
        remoteDataSource: NotificationRemoteDataSource,
        internetConnectionChecker: InternetConnectionChecker,
    ): NotificationRepository {
        return NotificationRepositoryImp(
            remoteDataSource = remoteDataSource,
            connectionChecker = internetConnectionChecker,
        )
    }

    @Provides
    @Singleton
    fun provideNotificationManagerRepository(
        localDataSource: NotificationLocalDataSource
    ): NotificationManagerRepository {
        return NotificationManagerRepositoryImp(
            localDataSource = localDataSource
        )
    }
}