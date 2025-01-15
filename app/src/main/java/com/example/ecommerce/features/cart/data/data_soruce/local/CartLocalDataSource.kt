package com.example.ecommerce.features.cart.data.data_soruce.local

import com.example.ecommerce.core.database.data.entities.cart.CartWithItems
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity
import com.example.ecommerce.features.cart.data.models.CartResponseModel

interface CartLocalDataSource {
    suspend fun insertCartWithItems(cartResponseModel: CartResponseModel)
    suspend fun getCart():CartWithItems
    suspend fun removeItem(keyItem:String)
    suspend fun updateItemsCart(cartResponseModel: CartResponseModel)

}