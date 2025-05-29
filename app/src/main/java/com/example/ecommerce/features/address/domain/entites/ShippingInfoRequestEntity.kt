package com.example.ecommerce.features.address.domain.entites

import com.google.gson.annotations.SerializedName

data class ShippingInfoRequestEntity(
    @SerializedName("first_name") val firstName: String= "",
    @SerializedName("last_name") val lastName: String= "",
    @SerializedName("address_1") val address: String= "",
    @SerializedName("city") val city: String= "",
    @SerializedName("state") val state: String= "",
    @SerializedName("postcode") val postCode: String= "",
    @SerializedName("country") val country: String= "",
)
