package com.example.ecommerce.features.authentication.presentation.state

data class SignUpState(
    val userName: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
)