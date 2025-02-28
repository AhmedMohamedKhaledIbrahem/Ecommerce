package com.example.ecommerce.features.orders.domain.use_case.create_order

import com.example.ecommerce.features.orders.domain.entities.OrderRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderResponseEntity
import com.example.ecommerce.features.orders.domain.repository.OrderRepository
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) : ICreateOrderUseCase {
    override suspend fun invoke(orderRequestEntity: OrderRequestEntity): OrderResponseEntity {
        return orderRepository.createOrder(orderRequestEntity)
    }
}