package com.example.ecommerce.features.orders.data.data_source.local

import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems
import com.example.ecommerce.features.orders.data.models.OrderResponseModel

interface OrderLocalDataSource {
    suspend fun insertOrderWithItem(orderResponseModel: OrderResponseModel)
    suspend fun getOrders(): List<OrderWithItems>
    suspend fun clearOrders()

}