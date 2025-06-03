package com.example.ecommerce.features.authentication.presentation.state

data class ConfirmPasswordState(
    val userid: Int = -1,
    val otp: Int = -1,
    val newPassword: String = "",
    val isLoading: Boolean = false,
    val isFinished: Boolean = false
)
