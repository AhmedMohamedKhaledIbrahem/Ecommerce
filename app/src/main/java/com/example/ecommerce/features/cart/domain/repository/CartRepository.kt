package com.example.ecommerce.features.cart.domain.repository

import com.example.ecommerce.core.database.data.entities.cart.CartWithItems
import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity

interface CartRepository {
    suspend fun addItem(addItemParams: AddItemRequestEntity)
    suspend fun getCart():CartWithItems
    suspend fun removeItem(keyItem:String)
}