package com.example.ecommerce.features.cart.presentation.state




data class CheckOutState(
    val customerId: Int = 0,
    val addressId: Int = 0,
    val isCheckingOut: Boolean = false,
)
