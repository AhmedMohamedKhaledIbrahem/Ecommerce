package com.example.ecommerce.features.cart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.ItemAddedSuccessfully
import com.example.ecommerce.core.constants.ItemRemovedSuccessfully
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.cart.domain.use_case.add_item.IAddItemUseCase
import com.example.ecommerce.features.cart.domain.use_case.get_cart.IGetCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.remove_Item.IRemoveItemUseCase
import com.example.ecommerce.features.cart.domain.use_case.update_quantity.IUpdateQuantityUseCase
import com.example.ecommerce.features.cart.presentation.event.CartEvent
import com.example.ecommerce.features.cart.presentation.state.CartLoadState
import com.example.ecommerce.features.cart.presentation.state.CartState
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
class CartViewModel @Inject constructor(
    private val addItemUseCase: IAddItemUseCase,
    private val getCartUseCase: IGetCartUseCase,
    private val removeItemUseCase: IRemoveItemUseCase,
    private val updateQuantityUseCase: IUpdateQuantityUseCase,
) : ViewModel() {


    private val _cartEvent: Channel<UiEvent> = Channel()
    val cartEvent = _cartEvent.receiveAsFlow()

    private val _cartState = MutableStateFlow(CartState())
    val cartState: StateFlow<CartState> get() = _cartState.asStateFlow()

    private val _cartLoadState = MutableStateFlow(CartLoadState())
    val cartLoadState: StateFlow<CartLoadState> get() = _cartLoadState.asStateFlow()

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.LoadCart -> {
                getCart()
            }

            is CartEvent.Input.AddItem -> {
                _cartState.update { it.copy(addItemParams = event.addItemParams) }
            }

            is CartEvent.Input.ItemId -> {
                _cartState.update { it.copy(itemId = event.itemId) }
            }

            is CartEvent.Input.IncreaseQuantity -> {
                _cartState.update { it.copy(increase = event.increase) }
            }

            is CartEvent.Input.DecreaseQuantity -> {
                _cartState.update { it.copy(decrease = event.decrease) }
            }

            is CartEvent.Input.RemoveItem -> {
                _cartState.update { it.copy(removeItem = event.keyItem) }
            }

            is CartEvent.Button.IncreaseQuantity -> {
                increaseQuantity()
            }

            is CartEvent.Button.DecreaseQuantity -> {
                decreaseQuantity()
            }

            is CartEvent.Button.AddToCart -> {
                addItem()
            }

            is CartEvent.Button.RemoveItem -> {
                removeItem()
            }

        }
    }

    private fun increaseQuantity() {
        viewModelScope.launch {
            try {
                updateQuantityUseCase(
                    itemId = cartState.value.itemId,
                    newQuantity = cartState.value.increase
                )
            } catch (failures: Failures) {
                val message = mapFailureMessage(failures)
                _cartEvent.send(UiEvent.ShowSnackBar(message = message))
            } catch (e: Exception) {
                _cartEvent.send(UiEvent.ShowSnackBar(message = e.message ?: "Unknown Error"))
            }
        }
    }

    private fun decreaseQuantity() {
        viewModelScope.launch {
            try {
                updateQuantityUseCase(
                    itemId = cartState.value.itemId,
                    newQuantity = cartState.value.decrease
                )

            } catch (failures: Failures) {
                val message = mapFailureMessage(failures)
                _cartEvent.send(UiEvent.ShowSnackBar(message = message))
            } catch (e: Exception) {
                _cartEvent.send(UiEvent.ShowSnackBar(message = e.message ?: "Unknown Error"))

            }
        }
    }

    private fun removeItem() {
        viewModelScope.launch {
            try {
                _cartLoadState.update { it.copy(isRemoveLoading = true) }
                removeItemUseCase(keyItem = cartState.value.removeItem)
                _cartEvent.send(UiEvent.ShowSnackBar(message = ItemRemovedSuccessfully))
            } catch (failures: Failures) {
                val message = mapFailureMessage(failures)
                _cartEvent.send(UiEvent.ShowSnackBar(message = message))
            } catch (e: Exception) {
                _cartEvent.send(
                    UiEvent.ShowSnackBar(message = e.message ?: "Unknown Error"),
                )

            } finally {
                _cartLoadState.update { it.copy(isRemoveLoading = false) }
            }
        }
    }

    private fun addItem() {
        viewModelScope.launch {
            try {
                _cartLoadState.update { it.copy(isAddLoading = true) }
                addItemUseCase(addItemParams = cartState.value.addItemParams)
                _cartEvent.send(
                    UiEvent.CombinedEvents(
                        listOf(
                            UiEvent.ShowSnackBar(message = ItemAddedSuccessfully),
                            UiEvent.Navigation.Cart(destinationId = R.id.cartFragment)
                        )
                    )
                )
            } catch (failures: Failures) {
                val message = mapFailureMessage(failures)
                _cartEvent.send(UiEvent.ShowSnackBar(message = message))
            } catch (e: Exception) {
                _cartEvent.send(UiEvent.ShowSnackBar(message = e.message ?: "Unknown Error"))
            } finally {
                _cartLoadState.update { it.copy(isAddLoading = false) }
            }
        }
    }

    private fun getCart() {
        viewModelScope.launch {
            try {
                _cartLoadState.update { it.copy(isGetLoading = true) }
                val cartWithItems = getCartUseCase()
                _cartState.update { it.copy(cartWithItems = cartWithItems) }
            } catch (failures: Failures) {
                val message = mapFailureMessage(failures)
                _cartEvent.send(UiEvent.ShowSnackBar(message = message))
            } catch (e: Exception) {
                _cartEvent.send(UiEvent.ShowSnackBar(message = e.message ?: "Unknown Error"))

            } finally {
                _cartLoadState.update { it.copy(isGetLoading = false) }
            }
        }
    }
}