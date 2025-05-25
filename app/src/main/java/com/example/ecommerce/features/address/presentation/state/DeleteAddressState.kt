package com.example.ecommerce.features.address.presentation.state

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity

data class DeleteAddressState(
    val customerAddressEntity: CustomerAddressEntity = CustomerAddressEntity(),
    val isDeleteAddressLoading: Boolean = false,
    val isDeleteAllAddressLoading: Boolean = false,
)
