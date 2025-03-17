package com.example.ecommerce.core.database.data.entities.cart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey val cartId: String,
    val cartHash: String,
)