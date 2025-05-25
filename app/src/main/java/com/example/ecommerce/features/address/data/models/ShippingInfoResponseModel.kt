package com.example.ecommerce.features.address.data.models

import com.google.gson.annotations.SerializedName

data class ShippingInfoResponseModel(
    @SerializedName("first_name") val firstName: String= "",
    @SerializedName("last_name") val lastName: String= "",
    @SerializedName("address_1") val address: String= "",
    @SerializedName("city") val city: String= "",
    @SerializedName("state") val state: String= "",
    @SerializedName("postcode") val postCode: String= "",
    @SerializedName("country") val country: String= "",
)
