package com.example.ecommerce.features.cart.data.data_soruce

import com.example.ecommerce.core.constants.ADD_ITEM_TO_CART_END_POINT
import com.example.ecommerce.core.constants.CLEAR_CART_END_POINT
import com.example.ecommerce.core.constants.GET_CART_END_POINT
import com.example.ecommerce.core.constants.REMOVE_ITEM_END_POINT
import com.example.ecommerce.features.cart.data.models.AddItemRequestModel
import com.example.ecommerce.features.cart.data.models.CartResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CartApi {
    @GET(GET_CART_END_POINT)
    suspend fun getCart(): Response<CartResponseModel>

    @POST(ADD_ITEM_TO_CART_END_POINT)
    suspend fun addItemToCart(@Body request: AddItemRequestModel): Response<CartResponseModel>

    @DELETE(REMOVE_ITEM_END_POINT)
    suspend fun removeItem(@Path("cart_item_key") keyItem: String)

    @POST(CLEAR_CART_END_POINT)
    suspend fun clearCart(): Response<Unit>


}