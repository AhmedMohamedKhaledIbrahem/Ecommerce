package com.example.ecommerce.features.orders.data.data_source

import com.example.ecommerce.features.orders.data.models.OrderRequestModel
import com.example.ecommerce.features.orders.data.models.OrderResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OrderApi {
    @POST("wp-json/custom/v1/orders")
    suspend fun createOrder(@Body orderRequestModel: OrderRequestModel): Response<OrderResponseModel>

    @GET("wp-json/custom/v1/user-orders")
    suspend fun getOrders(): Response<List<OrderResponseModel>>

}