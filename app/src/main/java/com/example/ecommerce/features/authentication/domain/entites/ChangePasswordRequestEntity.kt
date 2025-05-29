package com.example.ecommerce.features.authentication.domain.entites

data class ChangePasswordRequestEntity(
    val userId: Int,
    val password: String
)
