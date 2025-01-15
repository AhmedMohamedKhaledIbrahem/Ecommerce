package com.example.ecommerce.core.database.data.entities.cart

import androidx.room.Embedded
import androidx.room.Relation

data class CartWithItems(
    @Embedded val cart:CartEntity,
    @Relation(
        parentColumn = "cartId",
        entityColumn = "cartId"
    )
    val items: List<ItemCartEntity>
)
