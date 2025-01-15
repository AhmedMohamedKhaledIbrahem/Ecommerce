package com.example.ecommerce.features.cart.data.mapper

import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity
import com.example.ecommerce.features.cart.data.models.CartItemResponseModel

object ItemMapper {
    fun mapToEntity(cartItemResponseModel: CartItemResponseModel, cartId: String): ItemCartEntity {
        return ItemCartEntity(
            itemId = cartItemResponseModel.id,
            itemHashKey = cartItemResponseModel.itemKey,
            name = cartItemResponseModel.name,
            image = cartItemResponseModel.imageItemLink.replace("localhost", "10.0.0.106"),
            price = (cartItemResponseModel.price.toInt() / 100).toString(),
            cartId = cartId,
            quantity = cartItemResponseModel.quantity.value
        )
    }
}