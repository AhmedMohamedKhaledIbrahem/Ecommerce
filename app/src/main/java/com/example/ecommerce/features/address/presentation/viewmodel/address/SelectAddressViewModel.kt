package com.example.ecommerce.features.address.presentation.viewmodel.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.extension.eventHandler
import com.example.ecommerce.core.extension.performUseCaseOperation
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.address.domain.usecases.getselectaddress.IGetSelectAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.selectaddress.ISelectAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.unselectaddress.IUnSelectAddressUseCase
import com.example.ecommerce.features.address.presentation.event.SelectAddressEvent
import com.example.ecommerce.features.address.presentation.state.SelectAddressState
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
class SelectAddressViewModel @Inject constructor(
    private val selectAddressUseCase: ISelectAddressUseCase,
    private val unSelectAddressUseCase: IUnSelectAddressUseCase,
    private val getSelectAddressUseCase: IGetSelectAddressUseCase,
) : ViewModel() {

    private val _selectAddressEvent: Channel<UiEvent> = Channel()
    val selectAddressEvent = _selectAddressEvent.receiveAsFlow()

    private val _selectAddressState = MutableStateFlow(SelectAddressState())
    val selectAddressState: StateFlow<SelectAddressState> = _selectAddressState.asStateFlow()

    fun onEvent(event: SelectAddressEvent) {
        eventHandler(event) { evt ->
            when (evt) {
                is SelectAddressEvent.SetCustomerAddressId -> {
                    _selectAddressState.update { it.copy(customerAddressId = evt.customerAddressId) }
                }

                is SelectAddressEvent.SetSelected -> {
                    _selectAddressState.update { it.copy(isSelected = evt.isSelected) }
                }

                is SelectAddressEvent.SelectAddress -> {
                    selectAddress()
                }

                is SelectAddressEvent.UnSelectAddress -> {
                    unSelectAddress()
                }

                is SelectAddressEvent.GetSelectAddress -> {
                    getSelectAddress()
                }


            }

        }
    }

    private fun selectAddress() = performUseCaseOperation(
        loadingUpdater = { isLoading ->
            _selectAddressState.update { it.copy(isSelectAddressLoading = isLoading) }
        },
        useCase = {
            val customerAddressId = selectAddressState.value.customerAddressId
            val isSelected = selectAddressState.value.isSelected
            if (!validateAddressId(customerAddressId)) {
                _selectAddressState.update {
                    it.copy(
                        isSelectAddressLoading = false
                    )
                }
                return@performUseCaseOperation
            } else if (isSelected == 1) {
                _selectAddressState.update {
                    it.copy(
                        isSelectAddressLoading = false
                    )
                }
                _selectAddressEvent.send(
                    UiEvent.ShowSnackBar(resId = R.string.address_already_selected))
                    return@performUseCaseOperation
            }
            selectAddressUseCase(customerAddressId)
            _selectAddressEvent.send(
                UiEvent.ShowSnackBar(
                  resId=R.string.address_selected_successfully
                )
            )
        },
        onFailure = { failure ->
            val errorMessage = mapFailureMessage(failure)
            _selectAddressEvent.send(UiEvent.ShowSnackBar(errorMessage))
        },
        onException = { exception ->
            _selectAddressEvent.send(UiEvent.ShowSnackBar(exception.message ?: Unknown_Error))
        },
    )

    private fun unSelectAddress() = performUseCaseOperation(
        loadingUpdater = { isLoading ->
            _selectAddressState.update { it.copy(isUnSelectAddressLoading = isLoading) }
        },
        useCase = {
            val isSelected = selectAddressState.value.isSelected
            val customerAddressId = selectAddressState.value.customerAddressId
            if (!validateAddressId(customerAddressId)) {
                _selectAddressState.update {
                    it.copy(
                        isUnSelectAddressLoading = false
                    )
                }

                return@performUseCaseOperation
            } else if (isSelected == 1) {
                _selectAddressState.update {
                    it.copy(
                        isUnSelectAddressLoading = false
                    )
                }
                return@performUseCaseOperation
            }
            unSelectAddressUseCase(customerAddressId)
            _selectAddressEvent.send(
                UiEvent.ShowSnackBar(
                  resId=R.string.address_unselected_successfully
                )
            )
        },
        onFailure = { failure ->
            val errorMessage = mapFailureMessage(failure)
            _selectAddressEvent.send(UiEvent.ShowSnackBar(errorMessage))
        },
        onException = { exception ->
            _selectAddressEvent.send(UiEvent.ShowSnackBar(exception.message ?: Unknown_Error))
        },
    )


    private fun getSelectAddress() = performUseCaseOperation(
        loadingUpdater = { isLoading ->
            _selectAddressState.update {
                it.copy(
                    isGetSelectAddressLoading = isLoading
                )
            }
        },
        useCase = {
            val customerAddressId = selectAddressState.value.customerAddressId
            if (!validateAddressId(customerAddressId)) {
                _selectAddressState.update {
                    it.copy(
                        isGetSelectAddressLoading = false
                    )
                }
                return@performUseCaseOperation
            }
            getSelectAddressUseCase(customerAddressId)
        },
        onFailure = { failure ->
            val errorMessage = mapFailureMessage(failure)
            _selectAddressEvent.send(UiEvent.ShowSnackBar(errorMessage))
        },
        onException = { exception ->
            _selectAddressEvent.send(UiEvent.ShowSnackBar(exception.message ?: Unknown_Error))
        },
    )


    private fun validateAddressId(customerAddressId: Int): Boolean {
        if (customerAddressId == INVALID_ADDRESS_ID) {
            viewModelScope.launch {
                _selectAddressEvent.send(UiEvent.ShowSnackBar(resId = R.string.please_select_address))
            }
            return false
        }
        return true
    }


    companion object {
        private const val INVALID_ADDRESS_ID = -1

    }

}