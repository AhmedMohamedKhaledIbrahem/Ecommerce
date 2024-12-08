package com.example.ecommerce.features.address.domain.entites

import com.google.gson.annotations.SerializedName

data class AddressRequestEntity(
    @SerializedName("billing") val billing: BillingInfoRequestEntity? = null ?: BillingInfoRequestEntity(),
    @SerializedName("shipping") val shipping: ShippingInfoRequestEntity? = null ?: ShippingInfoRequestEntity()
)
