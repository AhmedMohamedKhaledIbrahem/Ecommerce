package com.example.ecommerce.features.orders.domain.use_case.save_order_locally

import com.example.ecommerce.features.orders.domain.entities.OrderResponseEntity
import com.example.ecommerce.features.orders.domain.repository.OrderRepository
import javax.inject.Inject

class SaveOrderLocallyUseCase @Inject constructor(
    private val repository: OrderRepository
) : ISaveOrderLocallyUseCase {
    override suspend fun invoke(orderResponseEntity: OrderResponseEntity) {
        repository.saveOrderLocally(orderResponseEntity = orderResponseEntity)
    }
}