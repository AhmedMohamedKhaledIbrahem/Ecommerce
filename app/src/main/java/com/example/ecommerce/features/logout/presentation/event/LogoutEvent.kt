package com.example.ecommerce.features.logout.presentation.event

sealed class LogoutEvent {
    data class FcmTokenInput(val fcmToken: String) : LogoutEvent()
    data object LogoutButton : LogoutEvent()
}