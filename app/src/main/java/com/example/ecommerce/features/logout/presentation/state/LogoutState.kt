package com.example.ecommerce.features.logout.presentation.state

data class LogoutState(
    val isLoading: Boolean = false,
    val fcmToken: String = "",
)
