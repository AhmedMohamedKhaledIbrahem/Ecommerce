package com.example.ecommerce.features.orders.domain.use_case.create_order

import com.example.ecommerce.features.orders.domain.entities.OrderRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderResponseEntity

interface ICreateOrderUseCase {
    suspend operator fun invoke(orderRequestEntity: OrderRequestEntity): OrderResponseEntity
}