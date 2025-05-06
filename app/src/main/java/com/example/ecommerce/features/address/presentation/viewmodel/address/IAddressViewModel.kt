package com.example.ecommerce.features.address.presentation.viewmodel.address

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.ui.UiState
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IAddressViewModel {
    val addressEvent: SharedFlow<UiState<Any>>
    val addressState: StateFlow<List<CustomerAddressEntity>>
    fun updateAddress(id: Int, updateAddressParams: AddressRequestEntity)
    fun getAddress()
    fun deleteAllAddress()
    fun getSelectAddress(customerId: Int)
    fun selectAddress(customerId: Int)
    fun unSelectAddress(customerId: Int)
    fun deleteAddress(customerAddressEntity: CustomerAddressEntity)
    fun insertAddress(addressParams: AddressRequestEntity)
    fun <T> addressUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    )


}