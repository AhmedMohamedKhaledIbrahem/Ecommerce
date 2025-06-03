package com.example.ecommerce.features.notification.presentation.event

sealed class NotificationEvent {
    data class AddFcmTokenDevice(val token: String) : NotificationEvent()
    data object OnAddFcmTokenDevice : NotificationEvent()
}