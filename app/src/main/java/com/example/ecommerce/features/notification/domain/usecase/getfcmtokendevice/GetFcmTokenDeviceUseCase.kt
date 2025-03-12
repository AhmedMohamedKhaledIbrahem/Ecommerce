package com.example.ecommerce.features.notification.domain.usecase.getfcmtokendevice

import com.example.ecommerce.features.notification.domain.repository.NotificationManagerRepository
import javax.inject.Inject

class GetFcmTokenDeviceUseCase @Inject constructor(
    private val repository: NotificationManagerRepository
) : IGetFcmTokenDeviceUseCase {
    override suspend fun invoke(): String? {
        return repository.getFcmTokenDevice()
    }
}