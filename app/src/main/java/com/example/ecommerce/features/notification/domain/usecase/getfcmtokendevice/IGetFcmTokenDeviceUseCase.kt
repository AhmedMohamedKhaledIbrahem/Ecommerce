package com.example.ecommerce.features.notification.domain.usecase.getfcmtokendevice

interface IGetFcmTokenDeviceUseCase {
    suspend operator fun invoke(): String?
}