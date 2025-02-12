package com.example.ecommerce.features.orders.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.orders.domain.entities.OrderRequestEntity
import com.example.ecommerce.features.orders.domain.use_case.create_order.ICreateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val createOrderUseCase: ICreateOrderUseCase
) : ViewModel(), IOrderViewModel {
    private val _orderState = MutableSharedFlow<UiState<Any>>(replay = 0)
    override val orderState: SharedFlow<UiState<Any>>
        get() = _orderState.asSharedFlow()

    override fun createOrder(orderRequest: OrderRequestEntity) {
        orderUiState(
            operation = { createOrderUseCase.invoke(orderRequestEntity = orderRequest) },
            onSuccess = { result ->
                _orderState.emit(UiState.Success(result, "createOrder"))
            },
            source = "createOrder"
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