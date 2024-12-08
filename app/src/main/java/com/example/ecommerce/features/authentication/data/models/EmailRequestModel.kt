package com.example.ecommerce.features.authentication.data.models

import com.google.gson.annotations.SerializedName

data class EmailRequestModel(
    @SerializedName("email") val email :String
)
