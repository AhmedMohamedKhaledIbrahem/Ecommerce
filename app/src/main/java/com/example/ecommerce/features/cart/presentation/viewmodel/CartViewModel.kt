package com.example.ecommerce.features.cart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.ui.state.UiState
import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity
import com.example.ecommerce.features.cart.domain.use_case.add_item.IAddItemUseCase
import com.example.ecommerce.features.cart.domain.use_case.clear_cart.IClearCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.get_cart.IGetCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.remove_Item.IRemoveItemUseCase
import com.example.ecommerce.features.cart.domain.use_case.update_item_cart.IUpdateItemsCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.update_quantity.IUpdateQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val addItemUseCase: IAddItemUseCase,
    private val getCartUseCase: IGetCartUseCase,
    private val removeItemUseCase: IRemoveItemUseCase,
    private val updateItemsCartUseCase: IUpdateItemsCartUseCase,
    private val updateQuantityUseCase: IUpdateQuantityUseCase,
    private val clearCartUseCase: IClearCartUseCase
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

    override fun updateItemsCart() {
        cartUiState(
            operation = { updateItemsCartUseCase() },
            onSuccess = { result -> _cartState.emit(UiState.Success(result, "updateItemsCart")) },
            source = "updateItemsCart"
        )
    }

    override fun updateQuantity(itemId: Int, newQuantity: Int) {
        cartUiState(
            operation = { updateQuantityUseCase(itemId = itemId, newQuantity = newQuantity) },
            onSuccess = { result -> _cartState.emit(UiState.Success(result, "updateQuantity")) },
            source = "updateQuantity"
        )
    }

    override fun removeItem(keyItem: String) {
        cartUiState(
            operation = { removeItemUseCase(keyItem = keyItem) },
            onSuccess = { result -> _cartState.emit(UiState.Success(result, "removeItem")) },
            source = "removeItem"
        )
    }

    override fun clearCart() {
        cartUiState(
            operation = { clearCartUseCase() },
            onSuccess = { result -> _cartState.emit(UiState.Success(result, "clearCart")) },
            source = "clearCart"
        )
    }

    override fun <T> cartUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    ) {
        viewModelScope.launch {
            _cartState.emit(UiState.Loading(source = source))
            try {
                val result = operation()
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