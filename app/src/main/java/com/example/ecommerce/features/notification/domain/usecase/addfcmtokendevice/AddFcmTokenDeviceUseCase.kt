package com.example.ecommerce.features.notification.domain.usecase.addfcmtokendevice

import com.example.ecommerce.features.notification.domain.repository.NotificationRepository
import javax.inject.Inject

class AddFcmTokenDeviceUseCase @Inject constructor(
    private val repository: NotificationRepository
) : IAddFcmTokenDeviceUseCase {
    override suspend fun invoke(token: String) {
        repository.saveToken(token = token)
    }

}