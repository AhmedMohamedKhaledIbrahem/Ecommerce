package com.example.ecommerce.features.authentication.data.models

import com.google.gson.annotations.SerializedName

data class ConfirmPasswordResetRequestModel(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("otp")
    val otp: Int,
    @SerializedName("new_password")
    val password: String
)
