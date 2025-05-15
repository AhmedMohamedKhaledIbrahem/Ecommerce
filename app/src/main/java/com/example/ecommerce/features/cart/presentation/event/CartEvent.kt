package com.example.ecommerce.features.cart.presentation.event

import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity

sealed class CartEvent {
    sealed class Input : CartEvent() {
        data class AddItem(val addItemParams: AddItemRequestEntity) : CartEvent()
        data class ItemId(val itemId: Int) : CartEvent()
        data class IncreaseQuantity(val increase: Int) : CartEvent()
        data class DecreaseQuantity(val decrease: Int) : CartEvent()
        data class RemoveItem(val keyItem: String) : CartEvent()
    }
    sealed class Button : CartEvent() {
        data object IncreaseQuantity : CartEvent()
        data object DecreaseQuantity: CartEvent()
        data object AddToCart: CartEvent()
        data object RemoveItem: CartEvent()

    }
    data object LoadCart : CartEvent()

}