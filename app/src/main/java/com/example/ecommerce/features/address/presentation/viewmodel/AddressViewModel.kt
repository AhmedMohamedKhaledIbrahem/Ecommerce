package com.example.ecommerce.features.address.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.usecases.checkupdateaddress.ICheckUpdateAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.getaddressbyid.IGetAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.updateaddress.IUpdateAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val updateAddressUseCase: IUpdateAddressUseCase,
    private val getAddressUseCase: IGetAddressUseCase,
    private val checkUpdateAddressUseCase: ICheckUpdateAddressUseCase,
) : ViewModel(), IAddressViewModel {

    private val _addressState = MutableSharedFlow<UiState<Any>>(replay = 0)
    override val addressState: SharedFlow<UiState<Any>> get() = _addressState.asSharedFlow()

    override fun updateAddress(updateAddressParams: AddressRequestEntity) {
        addressUiState(
            operation = { updateAddressUseCase(updateAddressParams) },
            onSuccess = { result ->
                _addressState.emit(UiState.Success(result, "updateAddress"))
            },
            source = "updateAddress"
        )
    }

    override fun getAddressById(id: Int) {
        addressUiState(
            operation = { getAddressUseCase(id = id) },
            onSuccess = { result ->
                _addressState.emit(UiState.Success(result, "getAddressById"))
            },
            source = "getAddressById"
        )
    }

    override fun checkUpdateAddress() {
        addressUiState(
            operation = { checkUpdateAddressUseCase() },
            onSuccess = { result ->
                _addressState.emit(UiState.Success(result, "checkUpdateAddress"))
            },
            source = "checkUpdateAddress"
        )
    }

    override fun <T> addressUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    ) {
        viewModelScope.launch {
            _addressState.emit(UiState.Loading(source))
            delay(3000)
            try {
                val result = withContext(Dispatchers.IO) { operation() }
                onSuccess(result)
            } catch (failure: Failures) {
                _addressState.emit(UiState.Error(mapFailureMessage(failure), source))
            } catch (e: Exception) {
                _addressState.emit(UiState.Error(e.message ?: "Unknown Error", source))
            }
        }
    }

    override suspend fun setUiState(source:String) {
        _addressState.emit(UiState.Loading(source))
    }

}