package com.example.ecommerce.features.cart.data.data_soruce.remote

import com.example.ecommerce.features.cart.data.models.AddItemRequestModel
import com.example.ecommerce.features.cart.data.models.CartResponseModel

interface CartRemoteDataSource {
    suspend fun getCart():CartResponseModel
    suspend fun addItemCart(addItemRequestModel: AddItemRequestModel):CartResponseModel
    suspend fun removeItem(itemHash:String)
    suspend fun clearCart()

}