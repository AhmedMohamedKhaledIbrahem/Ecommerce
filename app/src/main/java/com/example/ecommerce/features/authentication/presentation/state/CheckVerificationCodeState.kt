package com.example.ecommerce.features.authentication.presentation.state

data class CheckVerificationCodeState(
    val digit1: String = "",
    val digit2: String = "",
    val digit3: String = "",
    val digit4: String = "",
    val digit5: String = "",
    val digit6: String = "",
    val email: String = "",
    val isLoading: Boolean = false
)
