package com.example.ecommerce.features.orders.domain.repository

import com.example.ecommerce.features.orders.domain.entities.OrderRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderResponseEntity

interface OrderRepository {
    suspend fun createOrder(orderRequestEntity: OrderRequestEntity): OrderResponseEntity
}