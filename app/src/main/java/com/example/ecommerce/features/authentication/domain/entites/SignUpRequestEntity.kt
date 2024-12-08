package com.example.ecommerce.features.authentication.domain.entites

data class SignUpRequestEntity(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)
