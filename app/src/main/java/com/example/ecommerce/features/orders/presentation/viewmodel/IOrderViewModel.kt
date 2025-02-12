package com.example.ecommerce.features.orders.presentation.viewmodel

import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.orders.domain.entities.OrderRequestEntity
import kotlinx.coroutines.flow.SharedFlow

interface IOrderViewModel {
    val orderState: SharedFlow<UiState<Any>>
    fun createOrder(orderRequest: OrderRequestEntity)
    fun <T> orderUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    )
}