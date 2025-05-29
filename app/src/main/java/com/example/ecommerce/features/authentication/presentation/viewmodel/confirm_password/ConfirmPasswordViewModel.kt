package com.example.ecommerce.features.authentication.presentation.viewmodel.confirm_password

import androidx.lifecycle.ViewModel
import com.example.ecommerce.R
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.extension.eventHandler
import com.example.ecommerce.core.extension.performUseCaseOperation
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.authentication.domain.entites.ConfirmPasswordResetRequestEntity
import com.example.ecommerce.features.authentication.domain.usecases.confirm_password_reset.ConfirmPasswordChangeUseCase
import com.example.ecommerce.features.authentication.presentation.event.ConfirmPasswordEvent
import com.example.ecommerce.features.authentication.presentation.state.ConfirmPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ConfirmPasswordViewModel @Inject constructor(
    private val confirmPasswordUseCase: ConfirmPasswordChangeUseCase
) : ViewModel() {
    private val _confirmPasswordEvent: Channel<UiEvent> = Channel()
    val confirmPasswordEvent = _confirmPasswordEvent.receiveAsFlow()
    private val _confirmPasswordState = MutableStateFlow(ConfirmPasswordState())
    val confirmPasswordState: StateFlow<ConfirmPasswordState> = _confirmPasswordState.asStateFlow()
    fun onEvent(event: ConfirmPasswordEvent) {
        eventHandler(event = event) { evt ->
            when (evt) {
                is ConfirmPasswordEvent.UserIdInput -> {
                    _confirmPasswordState.update { it.copy(userid = evt.userId) }
                }

                is ConfirmPasswordEvent.OtpInput -> {
                    _confirmPasswordState.update { it.copy(otp = evt.otp) }
                }

                is ConfirmPasswordEvent.NewPasswordInput -> {
                    _confirmPasswordState.update { it.copy(newPassword = evt.newPassword) }
                }

                is ConfirmPasswordEvent.ConfirmPasswordButton -> {
                    confirmPassword()
                }
            }

        }
    }

    private fun confirmPassword() = performUseCaseOperation(
        loadingUpdater = { isLoading ->
            _confirmPasswordState.update { it.copy(isLoading = isLoading) }
        },
        useCase = {
            val userId = confirmPasswordState.value.userid
            val otp = confirmPasswordState.value.otp
            val newPassword = confirmPasswordState.value.newPassword
            val confirmPasswordRequestEntity = ConfirmPasswordResetRequestEntity(
                userId = userId,
                otp = otp,
                password = newPassword
            )
            val messageResponse = confirmPasswordUseCase.invoke(confirmPasswordRequestEntity)
            _confirmPasswordEvent.send(UiEvent.ShowSnackBar(message = messageResponse.message))
        },
        onFailure = { failure ->
            val mapFailureToMessage = mapFailureMessage(failure)
            _confirmPasswordEvent.send(UiEvent.ShowSnackBar(mapFailureToMessage))
        },
        onException = {
            _confirmPasswordEvent.send(UiEvent.ShowSnackBar(resId = R.string.change_password_error))
        }
    )
}