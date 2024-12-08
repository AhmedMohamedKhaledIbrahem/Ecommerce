package com.example.ecommerce.features.address.domain.entites

import com.google.gson.annotations.SerializedName


data class AddressResponseEntity(
    @SerializedName("id") val userId: Int,
    @SerializedName("message") val message: String? = null ?: "",
    @SerializedName("billing") val billing: BillingInfoResponseEntity? = null
        ?: BillingInfoResponseEntity(),
    @SerializedName("shipping") val shipping: ShippingInfoResponseEntity? = null
        ?: ShippingInfoResponseEntity()
)
