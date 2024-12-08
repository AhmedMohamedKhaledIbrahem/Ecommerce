package com.example.ecommerce.features.address.domain.entites

import com.google.gson.annotations.SerializedName

data class ShippingInfoRequestEntity(
    @SerializedName("first_name") val firstName: String? = null ?: "",
    @SerializedName("last_name") val lastName: String? = null ?: "",
    @SerializedName("address_1") val address: String? = null ?: "",
    @SerializedName("city") val city: String? = null ?: "",
    @SerializedName("state") val state: String? = null ?: "",
    @SerializedName("postcode") val postCode: String? = null ?: "",
    @SerializedName("country") val country: String? = null ?: "",
)
