package com.example.ecommerce.features.cart.data.mapper

import com.example.ecommerce.core.constants.IpHost
import com.example.ecommerce.core.constants.LocalHost
import com.example.ecommerce.core.database.data.entities.cart.CartWithItems
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity
import com.example.ecommerce.features.cart.data.models.CartItemResponseModel
import com.example.ecommerce.features.orders.domain.entities.LineItemRequestEntity

object ItemMapper {
    fun mapToEntity(cartItemResponseModel: CartItemResponseModel, cartId: String): ItemCartEntity {
        return ItemCartEntity(
            itemId = cartItemResponseModel.id,
            itemHashKey = cartItemResponseModel.itemKey,
            name = cartItemResponseModel.name,
            image = cartItemResponseModel.imageItemLink.replace(LocalHost, IpHost),
            price = (cartItemResponseModel.price.toInt() / 100).toString(),
            cartId = cartId,
            quantity = cartItemResponseModel.quantity.value
        )
    }

    fun mapCartWithItemsToLineItemRequest(cartWithItems: CartWithItems): List<LineItemRequestEntity> {
        return cartWithItems.items.map { lineItem ->
            LineItemRequestEntity(
                productId = lineItem.itemId,
                quantity = lineItem.quantity
            )
        }
    }
}