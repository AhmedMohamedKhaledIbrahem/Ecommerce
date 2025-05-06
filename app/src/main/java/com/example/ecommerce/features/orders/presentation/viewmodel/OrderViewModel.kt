package com.example.ecommerce.features.orders.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.ui.UiState
import com.example.ecommerce.features.orders.domain.entities.OrderRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderResponseEntity
import com.example.ecommerce.features.orders.domain.use_case.clear_orders.IClearOrderUseCase
import com.example.ecommerce.features.orders.domain.use_case.create_order.ICreateOrderUseCase
import com.example.ecommerce.features.orders.domain.use_case.get_orders.IGetOrdersUseCase
import com.example.ecommerce.features.orders.domain.use_case.save_order_locally.ISaveOrderLocallyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val createOrderUseCase: ICreateOrderUseCase,
    private val getOrdersUseCase: IGetOrdersUseCase,
    private val clearOrderUseCase: IClearOrderUseCase,
    private val saveOrderLocallyUseCase: ISaveOrderLocallyUseCase
) : ViewModel(), IOrderViewModel {


    private val _orderState = MutableSharedFlow<UiState<Any>>(replay = 0)
    override val orderState: SharedFlow<UiState<Any>>
        get() = _orderState.asSharedFlow()

    private val _orderItemState = MutableStateFlow<List<OrderWithItems>>(value = emptyList())
    override val orderItemState: StateFlow<List<OrderWithItems>>
        get() = _orderItemState.asStateFlow()

    override fun createOrder(orderRequest: OrderRequestEntity) {
        orderUiState(
            operation = { createOrderUseCase.invoke(orderRequestEntity = orderRequest) },
            onSuccess = { result ->
                _orderState.emit(UiState.Success(result, "createOrder"))
            },
            source = "createOrder"
        )
    }

    override fun saveOrderLocally(orderResponseEntity: OrderResponseEntity) {
        orderUiState(
            operation = { saveOrderLocallyUseCase.invoke(orderResponseEntity = orderResponseEntity) },
            onSuccess = { result ->
                _orderState.emit(UiState.Success(result, "saveOrderLocally"))
            },
            source = "saveOrderLocally"
        )
    }

    override fun getOrders() {
        orderUiState(
            operation = { getOrdersUseCase.invoke() },
            onSuccess = { result ->
                _orderItemState.value = result
                _orderState.emit(UiState.Success(result, "getOrders"))
            },
            source = "getOrders"
        )
    }

    override fun clearOrders() {
        orderUiState(
            operation = { clearOrderUseCase.invoke() },
            onSuccess = { result ->
                _orderState.emit(UiState.Success(result, "clearOrders"))
            },
            source = "clearOrders"
        )
    }

    override fun <T> orderUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    ) {
        viewModelScope.launch {
            _orderState.emit(UiState.Loading(source = source))
            try {
                val result = operation()
                onSuccess(result)
            } catch (failure: Failures) {
                _orderState.emit(
                    UiState.Error(
                        message = mapFailureMessage(failure),
                        source = source
                    )
                )
            } catch (e: Exception) {
                _orderState.emit(
                    UiState.Error(
                        message = e.message ?: "Unknown error",
                        source = source
                    )
                )
            }
        }
    }
}