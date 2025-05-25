package com.example.ecommerce.features.address.presentation.state

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity

data class AddressState(
    val addressList: List<CustomerAddressEntity> = emptyList(),
    val id: Int = 0,
    val addressRequestEntity: AddressRequestEntity = AddressRequestEntity(),
    val isUpdateLoading: Boolean = false,
    val isInsertLoading: Boolean = false,
    val isGetAllAddressLoading: Boolean = false,
)
