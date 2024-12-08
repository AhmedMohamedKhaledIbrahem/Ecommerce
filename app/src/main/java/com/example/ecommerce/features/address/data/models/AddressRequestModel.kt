package com.example.ecommerce.features.address.data.models

import com.google.gson.annotations.SerializedName

data class AddressRequestModel(
    @SerializedName("billing") val billing: BillingInfoRequestModel? = null
        ?: BillingInfoRequestModel(),
    @SerializedName("shipping") val shipping: ShippingInfoRequestModel? = null
        ?: ShippingInfoRequestModel()
)

