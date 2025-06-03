package com.example.ecommerce.features.orders.domain.use_case.fetch_orders

import com.example.ecommerce.features.orders.domain.repository.OrderRepository
import javax.inject.Inject

interface FetchOrderUseCase {
    suspend operator fun invoke()
}

class FetchOrderUseCaseImp @Inject constructor(
    private val repository: OrderRepository
) : FetchOrderUseCase {
    override suspend fun invoke() {
        repository.fetchOrders()
    }

}