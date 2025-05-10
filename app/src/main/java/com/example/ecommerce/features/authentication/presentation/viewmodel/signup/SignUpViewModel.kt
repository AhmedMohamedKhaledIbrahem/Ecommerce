package com.example.ecommerce.features.authentication.presentation.viewmodel.signup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.UserEmailSaveState
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.features.authentication.domain.usecases.sendverificationcode.ISendVerificationCodeUseCase
import com.example.ecommerce.features.authentication.domain.usecases.signup.ISignUpUseCase
import com.example.ecommerce.features.authentication.presentation.event.SignUpEvent
import com.example.ecommerce.features.authentication.presentation.state.SignUpState
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
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: ISignUpUseCase,
    private val sendVerificationCodeUseCase: ISendVerificationCodeUseCase,
) : ViewModel() {
    private val _signUpEvent: Channel<UiEvent> = Channel()
    val signUpEvent = _signUpEvent.receiveAsFlow()
    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> = _signUpState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000L),
        initialValue = SignUpState()
    )

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.Input.UserName -> {
                _signUpState.update { it.copy(userName = event.value) }
            }

            is SignUpEvent.Input.FirstName -> {
                _signUpState.update { it.copy(firstName = event.value) }
            }

            is SignUpEvent.Input.LastName -> {
                _signUpState.update { it.copy(lastName = event.value) }
            }

            is SignUpEvent.Input.Email -> {
                _signUpState.update { it.copy(email = event.value) }
            }

            is SignUpEvent.Input.Password -> {
                _signUpState.update { it.copy(password = event.value) }
            }

            is SignUpEvent.Button.SignUp -> {
                signUp()
            }

            is SignUpEvent.Button.SignIn -> {
                viewModelScope.launch {
                    _signUpEvent.send(UiEvent.Navigation.SignIn(R.id.loginFragment))
                }
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            try {
                _signUpState.update { it.copy(isLoading = true) }
                val signUpParams = SignUpRequestEntity(
                    username = signUpState.value.userName,
                    firstName = signUpState.value.firstName,
                    lastName = signUpState.value.lastName,
                    email = signUpState.value.email,
                    password = signUpState.value.password
                )
                signUpUseCase(signUpParams = signUpParams)
                val sendVerificationParams = EmailRequestEntity(email = signUpState.value.email)
                val messageResponse =
                    sendVerificationCodeUseCase(sendVerificationCodeParams = sendVerificationParams)
                _signUpEvent.send(
                    UiEvent.CombinedEvents(
                        listOf(
                            UiEvent.ShowSnackBar(message = messageResponse.message),
                            UiEvent.Navigation.CheckVerificationCode(
                                destinationId = R.id.checkVerificationCodeFragment,
                                args = signUpState.value.email
                            )
                        )
                    )
                )
            } catch (failure: Failures) {
                val mapFailureToMessage = mapFailureMessage(failures = failure)
                _signUpEvent.send(UiEvent.ShowSnackBar(message = mapFailureToMessage))
            } catch (e: Exception) {
                _signUpEvent.send(UiEvent.ShowSnackBar(message = e.message ?: "Unknown Error"))
            } finally {
                _signUpState.update { it.copy(isLoading = false) }
            }
        }
    }
}