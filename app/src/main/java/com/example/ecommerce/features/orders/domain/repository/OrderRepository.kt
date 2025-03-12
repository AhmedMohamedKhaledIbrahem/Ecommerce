package com.example.ecommerce.features.orders.domain.repository

import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems
import com.example.ecommerce.features.orders.domain.entities.OrderRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderResponseEntity

interface OrderRepository {
    suspend fun createOrder(orderRequestEntity: OrderRequestEntity): OrderResponseEntity
    suspend fun getOrders(): List<OrderWithItems>
    suspend fun saveOrderLocally(orderResponseEntity: OrderResponseEntity)
    suspend fun clearOrders()
}