package com.example.ecommerce.features.address.presentation.event

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity

sealed class DeleteAddressEvent {
    data class DeleteAddressInput(val customerAddressEntity: CustomerAddressEntity) :
        DeleteAddressEvent()

    data object DeleteAddressButton : DeleteAddressEvent()
    data object DeleteAllAddress : DeleteAddressEvent()
}