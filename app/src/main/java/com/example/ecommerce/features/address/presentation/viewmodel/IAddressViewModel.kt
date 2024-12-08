package com.example.ecommerce.features.address.presentation.viewmodel

import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import kotlinx.coroutines.flow.SharedFlow

interface IAddressViewModel {
    val addressState: SharedFlow<UiState<Any>>
    fun updateAddress(updateAddressParams: AddressRequestEntity)
    fun getAddressById(id: Int)
    fun checkUpdateAddress()
    fun <T> addressUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    )
   suspend fun setUiState(source:String)
}