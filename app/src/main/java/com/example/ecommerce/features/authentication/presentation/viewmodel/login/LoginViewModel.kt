package com.example.ecommerce.features.authentication.presentation.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.CheckYourEmail
import com.example.ecommerce.core.manager.customer.CustomerManager
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.usecases.login.ILoginUseCase
import com.example.ecommerce.features.authentication.domain.usecases.sendverificationcode.ISendVerificationCodeUseCase
import com.example.ecommerce.features.authentication.presentation.event.LoginEvent
import com.example.ecommerce.features.authentication.presentation.state.LoginState
import com.example.ecommerce.features.notification.domain.usecase.addfcmtokendevice.IAddFcmTokenDeviceUseCase
import com.example.ecommerce.features.notification.domain.usecase.getfcmtokendevice.IGetFcmTokenDeviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
class LoginViewModel @Inject constructor(
    private val loginUseCase: ILoginUseCase,
    private val sendVerificationCodeUseCase: ISendVerificationCodeUseCase,
    private val addFcmTokenDeviceUseCase: IAddFcmTokenDeviceUseCase,
    private val getFcmTokenDeviceUseCase: IGetFcmTokenDeviceUseCase,
    private val customerManager: CustomerManager,
) : ViewModel() {
    private val _loginEvent: Channel<UiEvent> = Channel()
    val loginEvent = _loginEvent.receiveAsFlow()
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = LoginState()
    )

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.UserNameInput -> {
                _loginState.update { it.copy(userName = event.value) }
            }

            is LoginEvent.PasswordInput -> {
                _loginState.update { it.copy(password = event.value) }
            }

            is LoginEvent.Button.SignIn -> {
                signIn()
            }

            is LoginEvent.Button.SignUp -> {
                viewModelScope.launch {
                    _loginEvent.send(UiEvent.Navigation.SignUp(R.id.signUpFragment))
                }
            }

            is LoginEvent.Button.ForgetPassword -> {
                viewModelScope.launch {
                    _loginEvent.send(UiEvent.Navigation.ForgetPassword(R.id.forgetPasswordFragment))
                }
            }

        }

    }

    private fun signIn() {
        viewModelScope.launch {
            try {
                _loginState.update { it.copy(isLoading = true) }
                val loginParams = AuthenticationRequestEntity(
                    userName = loginState.value.userName,
                    password = loginState.value.password
                )
                val response = loginUseCase.invoke(loginParams = loginParams)
                if (response.verificationStatues) {
                    val setCustomerAsync = async { customerManager.setCustomerId(response.userId) }
                    val getFcmTokenAsync = async { getFcmTokenDeviceUseCase.invoke() }
                    setCustomerAsync.await()
                    getFcmTokenAsync.await()?.let { addFcmTokenDeviceUseCase.invoke(it) }
                    _loginEvent.send(UiEvent.Navigation.Home(R.id.productFragment))
                } else {
                    val sendVerification = EmailRequestEntity(response.userEmail)
                    sendVerificationCodeUseCase.invoke(sendVerificationCodeParams = sendVerification)

                    _loginEvent.send(
                        UiEvent.CombinedEvents(
                            listOf(
                                UiEvent.ShowSnackBar(message = CheckYourEmail),
                                UiEvent.Navigation.CheckVerificationCode(
                                    destinationId = R.id.checkVerificationCodeFragment,
                                    args = response.userEmail
                                )
                            )
                        )
                    )
                }
            } catch (failure: Failures) {
                val mapFailureToMessage = mapFailureMessage(failures = failure)
                _loginEvent.send(UiEvent.ShowSnackBar(message = mapFailureToMessage))
            } catch (e: Exception) {
                _loginEvent.send(UiEvent.ShowSnackBar(message = e.message ?: "Unknown Error"))
            } finally {
                _loginState.update { it.copy(isLoading = false) }
            }
        }
    }
}