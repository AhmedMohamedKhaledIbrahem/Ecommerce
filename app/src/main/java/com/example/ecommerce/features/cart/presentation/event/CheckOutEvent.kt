package com.example.ecommerce.features.cart.presentation.event

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.features.orders.domain.entities.LineItemRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderRequestEntity

sealed class CheckOutEvent {
    sealed class Input : CheckOutEvent() {
        data class CustomerId(val customerId: Int) : Input()
        data class AddressId(val addressId: Int) : Input()

    }

    data object CheckoutButton : CheckOutEvent()
}