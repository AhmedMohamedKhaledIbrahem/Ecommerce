package com.example.ecommerce.features.notification.domain.usecase.addfcmtokendevice

interface IAddFcmTokenDeviceUseCase {
    suspend operator  fun  invoke(token: String)
}