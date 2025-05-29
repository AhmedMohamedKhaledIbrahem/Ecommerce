package com.example.ecommerce.features.authentication.presentation.state

data class ChangePasswordState(
    val isLoading: Boolean = false,
    val isFinished: Boolean = false,
    val password: String = "",
    val userId: Int = -1
)
