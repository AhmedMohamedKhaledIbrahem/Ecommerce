package com.example.ecommerce.features.address.presentation.event

sealed class SelectAddressEvent {
    data class SetCustomerAddressId(val customerAddressId: Int) : SelectAddressEvent()
    data class SetSelected(val isSelected: Int) : SelectAddressEvent()
    data object SelectAddress : SelectAddressEvent()
    data object UnSelectAddress : SelectAddressEvent()
    data object GetSelectAddress : SelectAddressEvent()

}