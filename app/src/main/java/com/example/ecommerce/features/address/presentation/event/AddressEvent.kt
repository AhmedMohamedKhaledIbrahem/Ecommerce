package com.example.ecommerce.features.address.presentation.event

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity

sealed class AddressEvent {
    sealed class Input : AddressEvent() {
        data class UpdateAddress(val id: Int, val addressRequestEntity: AddressRequestEntity) :
            Input()

        data class InsertAddress(val addressRequestEntity: AddressRequestEntity) : Input()

    }
    sealed class Button : AddressEvent() {
        data object UpdateAddressButton : Button()
        data object InsertAddressButton : Button()
    }
    data object LoadAllAddress : AddressEvent()


}