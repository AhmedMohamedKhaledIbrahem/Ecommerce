package com.example.ecommerce.features.notification.domain.usecase.deletefcmtokendevice

import com.example.ecommerce.features.notification.domain.repository.NotificationManagerRepository
import javax.inject.Inject

class DeleteFcmTokenDeviceUseCase @Inject constructor(
    private val repository: NotificationManagerRepository
):IDeleteFcmTokenDeviceUseCase {
    override suspend fun invoke() {
        repository.deleteFcmTokenDevice()
    }
}