package com.example.ecommerce.features.address.domain.entites

import com.google.gson.annotations.SerializedName

data class AddressRequestEntity(
    @SerializedName("billing") val billing: BillingInfoRequestEntity =  BillingInfoRequestEntity(),
    @SerializedName("shipping") val shipping: ShippingInfoRequestEntity =  ShippingInfoRequestEntity()
)
