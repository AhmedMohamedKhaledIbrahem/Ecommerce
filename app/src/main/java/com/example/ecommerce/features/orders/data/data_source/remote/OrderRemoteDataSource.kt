package com.example.ecommerce.features.orders.data.data_source.remote

import com.example.ecommerce.features.orders.data.models.OrderRequestModel
import com.example.ecommerce.features.orders.data.models.OrderResponseModel

interface OrderRemoteDataSource {
    suspend fun createOrder(orderRequestModel: OrderRequestModel): OrderResponseModel

}