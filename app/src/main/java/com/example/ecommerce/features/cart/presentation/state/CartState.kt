package com.example.ecommerce.features.cart.presentation.state

import com.example.ecommerce.core.database.data.entities.cart.CartEntity
import com.example.ecommerce.core.database.data.entities.cart.CartWithItems
import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity

data class CartState(
    val addItemParams: AddItemRequestEntity = AddItemRequestEntity(id = "", quantity = ""),
    val cartWithItems: CartWithItems = CartWithItems(
        cart = CartEntity(cartId = "", cartHash = ""),
        items = emptyList()
    ),
    val itemId: Int = 0,
    val increase: Int = 0,
    val decrease: Int = 0,
    val removeItem: String = "",
)
data class CartLoadState(
    val isGetLoading: Boolean = false,
    val isAddLoading: Boolean = false,
    val isRemoveLoading: Boolean = false,

)