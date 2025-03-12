package com.example.ecommerce.features.orders.domain.use_case.get_orders

import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems
import com.example.ecommerce.features.orders.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) : IGetOrdersUseCase {
    override suspend fun invoke(): List<OrderWithItems> {
        return orderRepository.getOrders()
    }
}