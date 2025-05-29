package com.example.ecommerce.features.authentication.presentation.state

data class LoginState(
    val userName: String = "",
    val password: String = "",
    var isLoading: Boolean = false,
)
