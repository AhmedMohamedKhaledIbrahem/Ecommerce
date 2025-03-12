package com.example.ecommerce.features.orders.domain.use_case.clear_orders

import com.example.ecommerce.features.orders.domain.repository.OrderRepository
import javax.inject.Inject

class ClearOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) : IClearOrderUseCase {
    override suspend fun invoke() {
        orderRepository.clearOrders()
    }
}