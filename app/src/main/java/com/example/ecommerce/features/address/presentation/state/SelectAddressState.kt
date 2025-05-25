package com.example.ecommerce.features.address.presentation.state

data class SelectAddressState(
    val customerAddressId: Int = -1,
    val isSelected: Int = 0,
    val isSelectAddressLoading: Boolean = false,
    val isUnSelectAddressLoading: Boolean = false,
    val isGetSelectAddressLoading: Boolean = false,
)

