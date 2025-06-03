package com.example.ecommerce.features.authentication.data.models

import com.google.gson.annotations.SerializedName

data class SignUpRequestModel(
    @SerializedName("username") val username: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("password") val password: String
)
