package com.example.ecommerce.features.orders.data.data_source

import com.example.ecommerce.core.constants.CREATE_ORDER_END_POINT
import com.example.ecommerce.core.constants.GET_ORDERS_END_POINT
import com.example.ecommerce.features.orders.data.models.OrderRequestModel
import com.example.ecommerce.features.orders.data.models.OrderResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OrderApi {
    @POST(CREATE_ORDER_END_POINT)
    suspend fun createOrder(@Body orderRequestModel: OrderRequestModel): Response<OrderResponseModel>

    @GET(GET_ORDERS_END_POINT)
    suspend fun getOrders(): Response<List<OrderResponseModel>>

}