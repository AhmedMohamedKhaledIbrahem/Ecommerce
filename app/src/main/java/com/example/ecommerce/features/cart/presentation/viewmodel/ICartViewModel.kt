package com.example.ecommerce.features.cart.presentation.viewmodel

import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity
import kotlinx.coroutines.flow.SharedFlow

interface ICartViewModel {
    val cartState: SharedFlow<UiState<Any>>
    fun addItem(addItemParams: AddItemRequestEntity)
    fun getCart()
    fun updateItemsCart()
    fun updateQuantity(itemId: Int, newQuantity: Int)
    fun removeItem(keyItem: String)
    fun clearCart()
    fun <T> cartUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    )
}