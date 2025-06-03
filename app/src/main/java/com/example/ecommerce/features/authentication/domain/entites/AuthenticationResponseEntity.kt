package com.example.ecommerce.features.authentication.domain.entites


data class AuthenticationResponseEntity(
    val token: String,
    val userId: Int,
    val userEmail: String,
    val phone: String,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val displayName: String,
    val roles: List<String>,
    val verificationStatues: Boolean,
    val expiredToken: Int
)
