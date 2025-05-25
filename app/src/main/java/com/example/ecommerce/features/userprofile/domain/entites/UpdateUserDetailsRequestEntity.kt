package com.example.ecommerce.features.userprofile.domain.entites

data class UpdateUserDetailsRequestEntity(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val displayName: String,
)
