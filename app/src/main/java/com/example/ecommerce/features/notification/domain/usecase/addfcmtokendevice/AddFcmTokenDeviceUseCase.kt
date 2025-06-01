package com.example.ecommerce.features.notification.domain.usecase.addfcmtokendevice

import com.example.ecommerce.features.notification.domain.entity.NotificationRequestEntity
import com.example.ecommerce.features.notification.domain.entity.NotificationResponseEntity
import com.example.ecommerce.features.notification.domain.repository.NotificationRepository
import javax.inject.Inject

class AddFcmTokenDeviceUseCase @Inject constructor(
    private val repository: NotificationRepository
) : IAddFcmTokenDeviceUseCase {
    override suspend fun invoke(notificationRequestParams: NotificationRequestEntity): NotificationResponseEntity {
        return repository.saveToken(notificationRequestParams = notificationRequestParams)
    }

}