package com.example.ecommerce.features.notification.domain.usecase.addfcmtokendevice

import com.example.ecommerce.features.notification.domain.entity.NotificationRequestEntity
import com.example.ecommerce.features.notification.domain.entity.NotificationResponseEntity

interface IAddFcmTokenDeviceUseCase {
    suspend operator  fun  invoke(notificationRequestParams: NotificationRequestEntity): NotificationResponseEntity
}