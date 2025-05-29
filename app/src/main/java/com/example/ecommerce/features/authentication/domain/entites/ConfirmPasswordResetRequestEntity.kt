package com.example.ecommerce.features.authentication.domain.entites

data class ConfirmPasswordResetRequestEntity(
    val userId: Int,
    val otp: Int,
    val password: String
)
