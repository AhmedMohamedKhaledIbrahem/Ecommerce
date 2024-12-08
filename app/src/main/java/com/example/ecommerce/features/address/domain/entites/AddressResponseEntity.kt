package com.example.ecommerce.features.address.domain.entites

data class AddressEntityResponse(
    val message: String? = null ?: "",
    val billing: BillingInfoEntityResponse? = null ?: BillingInfoEntityResponse(),
    val shipping: ShippingInfoEntityResponse? = null ?: ShippingInfoEntityResponse()
)
