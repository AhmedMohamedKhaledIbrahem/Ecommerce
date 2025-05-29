package com.example.ecommerce.features.authentication.presentation.state

data class ForgetPasswordState(
     val email: String = "",
     val isLoading: Boolean = false,
)
