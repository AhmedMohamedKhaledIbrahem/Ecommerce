package com.example.ecommerce.features.cart.data.data_soruce

import com.example.ecommerce.features.cart.data.models.AddItemRequestModel
import com.example.ecommerce.features.cart.data.models.CartResponseModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CartApi {
    @GET("wp-json/cocart/v2/cart")
    suspend fun getCart(): Response<CartResponseModel>

    @POST("wp-json/cocart/v2/cart/add-item")
    suspend fun addItemToCart(@Body request: AddItemRequestModel):Response<CartResponseModel>

    @DELETE("wp-json/cocart/v2/cart/item/{cart_item_key}")
    suspend fun removeItem(@Path("cart_item_key") keyItem: String)

    @POST("wp-json/cocart/v2/cart/clear")
    suspend fun clearCart() : Response<Unit>


}