package com.example.ecommerce.features.address.domain.entites

data class AddressEntityRequest(
    val firstName: String? = null ?: "",
    val lastName: String? = null ?: "",
    val email: String? = null ?: "",
    val userName: String? = null ?: "",
    val billing: BillingInfoEntityRequest? = null ?: BillingInfoEntityRequest(),
    val shipping: ShippingInfoEntityRequest? = null ?: ShippingInfoEntityRequest()
)
