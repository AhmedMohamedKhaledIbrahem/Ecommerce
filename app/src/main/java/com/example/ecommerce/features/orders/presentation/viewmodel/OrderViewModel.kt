package com.example.ecommerce.features.orders.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.extension.eventHandler
import com.example.ecommerce.core.extension.performUseCaseOperation
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.orders.domain.use_case.get_orders.IGetOrdersUseCase
import com.example.ecommerce.features.orders.presentation.event.OrderEvent
import com.example.ecommerce.features.orders.presentation.state.OrderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getOrdersUseCase: IGetOrdersUseCase,
) : ViewModel() {
    private val _orderEvent: Channel<UiEvent> = Channel()
    val orderEvent = _orderEvent.receiveAsFlow()
    private val _orderState = MutableStateFlow(OrderState())
    val orderState: StateFlow<OrderState> get() = _orderState.asStateFlow()

    fun onEvent(event: OrderEvent) {
        eventHandler(
            event = event,
            onEvent = { evt ->
                when (evt) {
                    is OrderEvent.LoadOrders -> {
                        getOrders()
                    }

                    is OrderEvent.OnOrderItemCardClick -> {
                        _orderState.update { it.copy(orderItem = evt.value) }
                    }
                    is OrderEvent.OnOrderCardClick -> {
                        orderItemCard()
                    }
                }

            })
    }

    private fun getOrders() = performUseCaseOperation(
        loadingUpdater = { isLoading ->
            _orderState.update { it.copy(isOrdersLoading = isLoading) }
        },
        useCase = {
            val orderItems = getOrdersUseCase.invoke()
            _orderState.update { it.copy(orders = orderItems) }
        },
        onFailure = {
            val mapFailureToMessage = mapFailureMessage(it)
            _orderEvent.send(UiEvent.ShowSnackBar(mapFailureToMessage))
        },
        onException = {
            _orderEvent.send(UiEvent.ShowSnackBar(it.message ?: Unknown_Error))
        },
    )

    private fun orderItemCard() {
        viewModelScope.launch {
            try {
                val orderItem = orderState.value.orderItem
                _orderEvent.send(
                    UiEvent.Navigation.OrderDetails(
                        destinationId = R.id.orderDetailsFragment,
                        args = orderItem
                    )
                )
            } catch (e: Exception) {
                _orderEvent.send(UiEvent.ShowSnackBar(e.message ?: Unknown_Error))
            }
        }
    }

}