package com.example.ecommerce.features.authentication.domain.entites

data class CheckVerificationRequestEntity(

    val email: String,
    // @SerializedName("userName")
    // val userName: String,
    val code: String,
)