package com.example.ecommerce.features.orders.domain.use_case.get_orders

import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems

interface IGetOrdersUseCase {
    suspend operator fun invoke():List<OrderWithItems>
}