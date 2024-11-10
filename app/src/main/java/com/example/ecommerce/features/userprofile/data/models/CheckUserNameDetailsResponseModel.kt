package com.example.ecommerce.features.userprofile.data.models

import com.google.gson.annotations.SerializedName

data class CheckUserNameDetailsResponseModel(
    @SerializedName("updated")
    val update:Boolean
)
