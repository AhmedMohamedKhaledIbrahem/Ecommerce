package com.example.ecommerce.features.address.presentation.viewmodel.address

import androidx.lifecycle.ViewModel
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.extension.eventHandler
import com.example.ecommerce.core.extension.performUseCaseOperation
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.address.domain.usecases.deleteaddress.IDeleteAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.deletealladdress.IDeleteAllAddressUseCase
import com.example.ecommerce.features.address.presentation.event.DeleteAddressEvent
import com.example.ecommerce.features.address.presentation.state.DeleteAddressState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DeleteAddressViewModel @Inject constructor(
    private val deleteAllAddressUseCase: IDeleteAllAddressUseCase,
    private val deleteAddressUseCase: IDeleteAddressUseCase,
) : ViewModel() {
    private val _deleteAddressEvent: Channel<UiEvent> = Channel()
    val deleteAddressEvent = _deleteAddressEvent.receiveAsFlow()

    private val _deleteAddressState = MutableStateFlow(DeleteAddressState())
    val deleteAddressState: StateFlow<DeleteAddressState> get() = _deleteAddressState.asStateFlow()

    fun onEvent(event: DeleteAddressEvent) {
        eventHandler(event) { evt ->
            when (evt) {
                is DeleteAddressEvent.DeleteAddressInput -> {
                    _deleteAddressState.update { it.copy(customerAddressEntity = evt.customerAddressEntity) }
                }

                is DeleteAddressEvent.DeleteAddressButton -> {
                    deleteAddressButton()
                }

                is DeleteAddressEvent.DeleteAllAddress -> {
                    deleteAllAddress()
                }

            }

        }
    }


    private fun deleteAddressButton() = performUseCaseOperation(
        loadingUpdater = { isLoading ->
            _deleteAddressState.update { it.copy(isDeleteAddressLoading = isLoading) }
        },
        useCase = {
            val customerAddressEntity = deleteAddressState.value.customerAddressEntity
            if (customerAddressEntity.id == INVALID_ADDRESS_ID) return@performUseCaseOperation
            deleteAddressUseCase(customerAddressEntity)
            //val message = context.getString(R.string.address_deleted_successfully)
            _deleteAddressEvent.send(UiEvent.ShowSnackBar(resId = R.string.address_deleted_successfully))
        },
        onFailure = {
            val mapFailureToMessage = mapFailureMessage(it)
            _deleteAddressEvent.send(UiEvent.ShowSnackBar(mapFailureToMessage))
        },
        onException = {
            _deleteAddressEvent.send(UiEvent.ShowSnackBar(it.message ?: Unknown_Error))
        },
    )

    private fun deleteAllAddress() = performUseCaseOperation(
        loadingUpdater = { isLoading ->
            _deleteAddressState.update { it.copy(isDeleteAllAddressLoading = isLoading) }
        },
        useCase = {
            deleteAllAddressUseCase()
        },
        onFailure = {
            val mapFailureToMessage = mapFailureMessage(it)
            _deleteAddressEvent.send(UiEvent.ShowSnackBar(mapFailureToMessage))
        },
        onException = {
            _deleteAddressEvent.send(UiEvent.ShowSnackBar(it.message ?: Unknown_Error))
        },
    )


    companion object {
        private const val INVALID_ADDRESS_ID = -1
    }
}