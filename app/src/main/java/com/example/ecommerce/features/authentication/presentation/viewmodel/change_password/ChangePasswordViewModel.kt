package com.example.ecommerce.features.authentication.presentation.viewmodel.change_password

import androidx.lifecycle.ViewModel
import com.example.ecommerce.R
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.extension.eventHandler
import com.example.ecommerce.core.extension.performUseCaseOperation
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.authentication.domain.entites.ChangePasswordRequestEntity
import com.example.ecommerce.features.authentication.domain.usecases.change_password.ChangePasswordUseCase
import com.example.ecommerce.features.authentication.presentation.event.ChangePasswordEvent
import com.example.ecommerce.features.authentication.presentation.state.ChangePasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
) : ViewModel() {
    private val _changePasswordEvent: Channel<UiEvent> = Channel()
    val changePasswordEvent = _changePasswordEvent.receiveAsFlow()

    private val _changePasswordState = MutableStateFlow(ChangePasswordState())
    val changePasswordState: StateFlow<ChangePasswordState> = _changePasswordState.asStateFlow()

    fun onEvent(event: ChangePasswordEvent) {
        eventHandler(event = event) { evt ->
            when (evt) {
                is ChangePasswordEvent.PasswordInput -> {
                    _changePasswordState.update { it.copy(password = evt.password) }
                }

                is ChangePasswordEvent.UserIdInput -> {
                    _changePasswordState.update { it.copy(userId = evt.userId) }
                }

                is ChangePasswordEvent.ChangePasswordButton -> {
                    changePassword()
                }
            }
        }
    }

    private fun changePassword() = performUseCaseOperation(
        loadingUpdater = { isLoading ->
            _changePasswordState.update { it.copy(isLoading = isLoading) }
        },
        useCase = {
            val password = changePasswordState.value.password
            val userId = changePasswordState.value.userId
            val changePasswordRequestEntity = ChangePasswordRequestEntity(
                userId = userId,
                password = password
            )
            val messageResponse = changePasswordUseCase.invoke(changePasswordRequestEntity)
            _changePasswordState.update { it.copy(isFinished = true) }
            _changePasswordEvent.send(UiEvent.ShowSnackBar(message = messageResponse.message))

        },
        onFailure = { failure ->
            val mapFailureToMessage = mapFailureMessage(failure)
            _changePasswordEvent.send(UiEvent.ShowSnackBar(mapFailureToMessage))
        },
        onException = {
            _changePasswordEvent.send(UiEvent.ShowSnackBar(resId = R.string.change_password_error))
        },
    )


}