package com.example.ecommerce.features.address.data.models

import com.google.gson.annotations.SerializedName

data class UpdateAddressResponseModel(
    @SerializedName("message") val message: String? = null ?: "",
)
