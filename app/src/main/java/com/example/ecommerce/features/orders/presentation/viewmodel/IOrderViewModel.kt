package com.example.ecommerce.features.orders.presentation.viewmodel

import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems
import com.example.ecommerce.core.ui.state.UiState
import com.example.ecommerce.features.orders.domain.entities.OrderRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderResponseEntity
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IOrderViewModel {
    val orderState: SharedFlow<UiState<Any>>
    val orderItemState:StateFlow<List<OrderWithItems>>
    fun createOrder(orderRequest: OrderRequestEntity)
    fun saveOrderLocally(orderResponseEntity: OrderResponseEntity)
    fun getOrders()
    fun clearOrders()
    fun <T> orderUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    )
}