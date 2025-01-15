package com.example.ecommerce.features.cart.data.mapper

import com.example.ecommerce.core.database.data.entities.cart.CartEntity
import com.example.ecommerce.features.cart.data.models.CartResponseModel

object CartMapper {
    fun mapToEntity(cartResponseModel: CartResponseModel): CartEntity {
        return CartEntity(
            cartId = cartResponseModel.cartKey,
        )

    }
}