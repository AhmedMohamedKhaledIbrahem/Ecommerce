package com.example.ecommerce.features.authentication.presentation.viewmodel.checkverificationcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.R
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.authentication.domain.entites.CheckVerificationRequestEntity
import com.example.ecommerce.features.authentication.domain.usecases.checkverificationcode.ICheckVerificationCodeUseCase
import com.example.ecommerce.features.authentication.presentation.event.CheckVerificationCodeEvent
import com.example.ecommerce.features.authentication.presentation.state.CheckVerificationCodeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckVerificationCodeViewModel @Inject constructor(
    private val checkVerificationCodeUseCase: ICheckVerificationCodeUseCase,
) : ViewModel() {
    private val _checkVerificationCodeEvent: Channel<UiEvent> = Channel()
    val checkVerificationCodeEvent = _checkVerificationCodeEvent.receiveAsFlow()
    private val _checkVerificationCodeState = MutableStateFlow(CheckVerificationCodeState())
    val checkVerificationCodeState: StateFlow<CheckVerificationCodeState> =
        _checkVerificationCodeState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = CheckVerificationCodeState()
        )


    fun onEvent(event: CheckVerificationCodeEvent) {
        when (event) {
            is CheckVerificationCodeEvent.Input.Digit1 -> {
                _checkVerificationCodeState.update { it.copy(digit1 = event.value) }
            }

            is CheckVerificationCodeEvent.Input.Digit2 -> {
                _checkVerificationCodeState.update { it.copy(digit2 = event.value) }
            }

            is CheckVerificationCodeEvent.Input.Digit3 -> {
                _checkVerificationCodeState.update { it.copy(digit3 = event.value) }
            }

            is CheckVerificationCodeEvent.Input.Digit4 -> {
                _checkVerificationCodeState.update { it.copy(digit4 = event.value) }
            }

            is CheckVerificationCodeEvent.Input.Digit5 -> {
                _checkVerificationCodeState.update { it.copy(digit5 = event.value) }
            }

            is CheckVerificationCodeEvent.Input.Digit6 -> {
                _checkVerificationCodeState.update { it.copy(digit6 = event.value) }
            }

            is CheckVerificationCodeEvent.Input.Email -> {
                _checkVerificationCodeState.update { it.copy(email = event.value) }
            }

            is CheckVerificationCodeEvent.VerifyButton -> {
                verifyCode()
            }
        }
    }

    fun verifyCode() {
        viewModelScope.launch {
            try {
                _checkVerificationCodeState.update { it.copy(isLoading = true) }
                val otp =
                    _checkVerificationCodeState.value.digit1
                        .plus(_checkVerificationCodeState.value.digit2)
                        .plus(_checkVerificationCodeState.value.digit3)
                        .plus(_checkVerificationCodeState.value.digit4)
                        .plus(_checkVerificationCodeState.value.digit5)
                        .plus(_checkVerificationCodeState.value.digit6)
                val checkVerificationCodeParams = CheckVerificationRequestEntity(
                    email = _checkVerificationCodeState.value.email,
                    code = otp
                )
                val message =
                    checkVerificationCodeUseCase.invoke(checkVerificationCodeParams = checkVerificationCodeParams)
                if (message.verified) {
                    _checkVerificationCodeEvent.send(
                        UiEvent.CombinedEvents(
                            listOf(
                                UiEvent.ShowSnackBar(message = message.message),
                                UiEvent.Navigation.Home(R.id.productFragment)
                            )
                        )
                    )
                } else {
                    _checkVerificationCodeEvent.send(
                        UiEvent.ShowSnackBar(message = message.message)
                    )
                }

            } catch (failure: Failures) {
                val mapFailureToMessage = mapFailureMessage(failures = failure)
                _checkVerificationCodeEvent.send(UiEvent.ShowSnackBar(message = mapFailureToMessage))
            } catch (e: Exception) {
                _checkVerificationCodeEvent.send(
                    UiEvent.ShowSnackBar(
                        message = e.message ?: "Unknown Error"
                    )
                )
            } finally {
                _checkVerificationCodeState.update { it.copy(isLoading = false) }
            }
        }
    }

}