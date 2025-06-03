package com.example.ecommerce.features.address.presentation.viewmodel.address

import androidx.lifecycle.ViewModel
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.extension.eventHandler
import com.example.ecommerce.core.extension.performUseCaseOperation
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.address.domain.usecases.getaddress.IGetAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.insertupdateaddress.IInsertAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.updateaddress.IUpdateAddressUseCase
import com.example.ecommerce.features.address.presentation.event.AddressEvent
import com.example.ecommerce.features.address.presentation.state.AddressState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val updateAddressUseCase: IUpdateAddressUseCase,
    private val getAddressUseCase: IGetAddressUseCase,
    private val insertAddressUseCase: IInsertAddressUseCase,
) : ViewModel() {

    private val _addressEvent: Channel<UiEvent> = Channel()
    val addressEvent = _addressEvent.receiveAsFlow()

    private val _addressState = MutableStateFlow(AddressState())
    val addressState: StateFlow<AddressState> get() = _addressState.asStateFlow()

    fun onEvent(event: AddressEvent) {
        eventHandler(event) { evt ->
            when (evt) {
                is AddressEvent.Input.UpdateAddress -> {
                    _addressState.update {
                        it.copy(
                            id = evt.id,
                            addressRequestEntity = evt.addressRequestEntity
                        )
                    }
                }

                is AddressEvent.Input.InsertAddress -> {
                    _addressState.update { it.copy(addressRequestEntity = evt.addressRequestEntity) }
                }

                is AddressEvent.Button.UpdateAddressButton -> {
                    updateAddress()
                }

                is AddressEvent.Button.InsertAddressButton -> {
                    insertAddress()
                }

                is AddressEvent.LoadAllAddress -> {
                    getAllAddress()
                }
            }

        }
    }

    private fun updateAddress() = performUseCaseOperation(
        loadingUpdater = { isLoading ->
            _addressState.update { it.copy(isUpdateLoading = isLoading) }
        },
        useCase = {
            val id = addressState.value.id
            val addressRequestEntity = addressState.value.addressRequestEntity
            if (id <= -1) {
                _addressState.update { it.copy(isUpdateLoading = false) }
                return@performUseCaseOperation
            }
            updateAddressUseCase(id, addressRequestEntity)
            _addressEvent.send(
                UiEvent.CombinedEvents(
                    listOf(
                        UiEvent.ShowSnackBar(resId = R.string.address_updated_successfully),
                        UiEvent.Navigation.Address(destinationId = R.id.addressFragment)
                    )
                )
            )
        },
        onFailure = { failure ->
            val message = mapFailureMessage(failure)
            _addressEvent.send(UiEvent.ShowSnackBar(message = message))
        },
        onException = { exception ->
            _addressEvent.send(UiEvent.ShowSnackBar(message = exception.message ?: Unknown_Error))
        }
    )

    private fun insertAddress() = performUseCaseOperation(
        loadingUpdater = { isLoading ->
            _addressState.update { it.copy(isInsertLoading = isLoading) }
        },
        useCase = {
            val addressRequestEntity = addressState.value.addressRequestEntity
            insertAddressUseCase(addressRequestEntity)
//            val message = context.getString(R.string.address_inserted_successfully)
            _addressEvent.send(
                UiEvent.CombinedEvents(
                    listOf(
                        UiEvent.ShowSnackBar(resId = R.string.address_inserted_successfully),
                        UiEvent.Navigation.Address(destinationId = R.id.addressFragment)
                    )
                )
            )
        },
        onFailure = { failure ->
            val message = mapFailureMessage(failure)
            _addressEvent.send(UiEvent.ShowSnackBar(message = message))
        },
        onException = { exception ->
            _addressEvent.send(UiEvent.ShowSnackBar(message = exception.message ?: Unknown_Error))
        }
    )

    private fun getAllAddress() = performUseCaseOperation(
        loadingUpdater = { isLoading ->
            _addressState.update { it.copy(isGetAllAddressLoading = isLoading) }
        },
        useCase = {
            val allAddress = getAddressUseCase()
            _addressState.update { it.copy(addressList = allAddress) }
        },
        onFailure = { failure ->
            val message = mapFailureMessage(failure)
            _addressEvent.send(UiEvent.ShowSnackBar(message = message))
        },
        onException = { exception ->
            _addressEvent.send(UiEvent.ShowSnackBar(message = exception.message ?: Unknown_Error))
        }
    )


}