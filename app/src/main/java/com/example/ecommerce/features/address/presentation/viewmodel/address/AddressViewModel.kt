package com.example.ecommerce.features.address.presentation.viewmodel.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.ui.UiState
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.usecases.deleteaddress.IDeleteAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.deletealladdress.IDeleteAllAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.getaddress.IGetAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.getselectaddress.IGetSelectAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.insertupdateaddress.IInsertAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.selectaddress.ISelectAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.unselectaddress.IUnSelectAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.updateaddress.IUpdateAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val updateAddressUseCase: IUpdateAddressUseCase,
    private val getAddressUseCase: IGetAddressUseCase,
    private val insertAddressUseCase: IInsertAddressUseCase,
    private val deleteAllAddressUseCase: IDeleteAllAddressUseCase,
    private val deleteAddressUseCase: IDeleteAddressUseCase,
    private val selectAddressUseCase: ISelectAddressUseCase,
    private val unSelectAddressUseCase: IUnSelectAddressUseCase,
    private val getSelectAddressUseCase: IGetSelectAddressUseCase

) : ViewModel(), IAddressViewModel {

    private val _addressEvent = MutableSharedFlow<UiState<Any>>(replay = 0)
    override val addressEvent: SharedFlow<UiState<Any>> get() = _addressEvent.asSharedFlow()
    private val _addressState = MutableStateFlow<List<CustomerAddressEntity>>(emptyList())
    override val addressState: StateFlow<List<CustomerAddressEntity>> get() = _addressState

    override fun updateAddress(id: Int, updateAddressParams: AddressRequestEntity) {
        addressUiState(
            operation = { updateAddressUseCase(id, updateAddressParams) },
            onSuccess = { result ->
                _addressEvent.emit(UiState.Success(result, "updateAddress"))
            },
            source = "updateAddress"
        )
    }

    override fun getAddress() {
        addressUiState(
            operation = { getAddressUseCase() },
            onSuccess = { result ->
                _addressState.value = result
                _addressEvent.emit(UiState.Success(result, "getAddress"))
            },
            source = "getAddress"
        )
    }

    override fun deleteAllAddress() {
        addressUiState(
            operation = { deleteAllAddressUseCase() },
            onSuccess = { result ->
                _addressEvent.emit(UiState.Success(result, "deleteAllAddress"))
            },
            source = "deleteAllAddress"
        )
    }

    override fun getSelectAddress(customerId: Int) {
        addressUiState(
            operation = { getSelectAddressUseCase(customerId) },
            onSuccess = { result ->
                _addressEvent.emit(UiState.Success(result, "getSelectAddress"))
            },
            source = "getSelectAddress"
        )
    }

    override fun selectAddress(customerId: Int) {
        addressUiState(
            operation = { selectAddressUseCase(customerId) },
            onSuccess = { result ->
                _addressEvent.emit(UiState.Success(result, "selectAddress"))
            },
            source = "selectAddress"
        )
    }

    override fun unSelectAddress(customerId: Int) {
        addressUiState(
            operation = { unSelectAddressUseCase(customerId) },
            onSuccess = { result ->
                _addressEvent.emit(UiState.Success(result, "unSelectAddress"))
            },
            source = "unSelectAddress"
        )
    }

    override fun deleteAddress(customerAddressEntity: CustomerAddressEntity) {
        addressUiState(
            operation = { deleteAddressUseCase(customerAddressEntity = customerAddressEntity) },
            onSuccess = { result ->
                _addressEvent.emit(UiState.Success(result, "deleteAddress"))
            },
            source = "deleteAddress"
        )
    }

    override fun insertAddress(addressParams: AddressRequestEntity) {
        addressUiState(
            operation = { insertAddressUseCase(addressParams) },
            onSuccess = { result ->
                _addressEvent.emit(UiState.Success(result, "insertAddress"))
            },
            source = "insertAddress"
        )
    }

    override fun <T> addressUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    ) {
        viewModelScope.launch {
            _addressEvent.emit(UiState.Loading(source))
            try {
                val result = operation()
                onSuccess(result)
            } catch (failure: Failures) {
                _addressEvent.emit(UiState.Error(mapFailureMessage(failure), source))
            } catch (e: Exception) {
                _addressEvent.emit(UiState.Error(e.message ?: "Unknown Error", source))
            }
        }
    }


}