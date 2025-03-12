package com.example.ecommerce.features.orders.domain.use_case.save_order_locally

import com.example.ecommerce.features.orders.domain.entities.OrderResponseEntity

interface ISaveOrderLocallyUseCase {
    suspend operator fun invoke(orderResponseEntity: OrderResponseEntity)
}