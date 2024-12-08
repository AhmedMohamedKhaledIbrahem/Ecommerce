package com.example.ecommerce.features.address.domain.entites

data class BillingInfoEntityResponse(
    val firstName: String? = null ?: "",
    val lastName: String? = null ?: "",
    val address: String? = null ?: "",
    val city: String? = null ?: "",
    val state: String? = null ?: "",
    val postCode: String? = null ?: "",
    val country: String? = null ?: "",
    val email: String? = null ?: "",
    val phone: String? = null ?: ""
)
