package com.example.ecommerce.features.authentication.domain.entites

import com.google.gson.annotations.SerializedName

data class CheckVerificationRequestEntity(
    @SerializedName("email")
    val email: String,
   // @SerializedName("userName")
   // val userName: String,
    @SerializedName("code")
    val code: String,
)