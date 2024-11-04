package com.example.ecommerce.features.authentication.domain.entites

import com.google.gson.annotations.SerializedName

data class SignUpRequestEntity(
    @SerializedName("username") val username: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)
