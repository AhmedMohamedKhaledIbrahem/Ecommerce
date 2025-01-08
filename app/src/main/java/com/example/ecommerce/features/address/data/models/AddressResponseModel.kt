package com.example.ecommerce.features.address.data.models

import com.google.gson.annotations.SerializedName

data class AddressResponseModel(
    @SerializedName("id") val userId: Int,
    @SerializedName("message") val message: String? = null ?: "",
    @SerializedName("data") val data: AddressDataResponseModel? = null

)
