package com.example.ecommerce.features.cart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity
import com.example.ecommerce.features.cart.domain.use_case.add_item.AddItemUseCase
import com.example.ecommerce.features.cart.domain.use_case.get_cart.GetCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.remove_Item.RemoveItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val addItemUseCase: AddItemUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val removeItemUseCase: RemoveItemUseCase,

    ) : ViewModel(), ICartViewModel {
    private val _cartState = MutableSharedFlow<UiState<Any>>(replay = 0)
    override val cartState: SharedFlow<UiState<Any>> get() = _cartState.asSharedFlow()
    override fun addItem(addItemParams: AddItemRequestEntity) {
        cartUiState(
            operation = { addItemUseCase(addItemParams = addItemParams) },
            onSuccess = { result -> _cartState.emit(UiState.Success(result, "addItem")) },
            source = "addItem"
        )
    }

    override fun getCart() {
        cartUiState(
            operation = { getCartUseCase() },
            onSuccess = { result -> _cartState.emit(UiState.Success(result, "getCart")) },
            source = "getCart"
        )
    }

    override fun removeItem(keyItem: String) {
        cartUiState(
            operation = { removeItemUseCase(keyItem = keyItem) },
            onSuccess = { result -> _cartState.emit(UiState.Success(result, "removeItem")) },
            source = "removeItem"
        )
    }

    private fun <T> cartUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    ) {
        viewModelScope.launch {
            _cartState.emit(UiState.Loading(source = source))
            try {
                val result = withContext(Dispatchers.IO) { operation() }
                onSuccess(result)
            } catch (failure: Failures) {
                _cartState.emit(
                    UiState.Error(
                        message = mapFailureMessage(failure),
                        source = source
                    )
                )
            } catch (e: Exception) {
                _cartState.emit(
                    UiState.Error(
                        message = "${e.message}",
                        source = source
                    )
                )
            }
        }
    }
}