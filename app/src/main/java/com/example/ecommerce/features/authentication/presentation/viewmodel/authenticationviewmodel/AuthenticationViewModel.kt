package com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.CheckVerificationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.features.authentication.domain.usecases.checkverificationcode.ICheckVerificationCodeUseCase
import com.example.ecommerce.features.authentication.domain.usecases.login.ILoginUseCase
import com.example.ecommerce.features.authentication.domain.usecases.logout.ILogoutUseCase
import com.example.ecommerce.features.authentication.domain.usecases.restpassword.IResetPasswordUseCase
import com.example.ecommerce.features.authentication.domain.usecases.sendverificationcode.ISendVerificationCodeUseCase
import com.example.ecommerce.features.authentication.domain.usecases.signup.ISignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val loginUseCase: ILoginUseCase,
    private val signUpUseCase: ISignUpUseCase,
    private val resetPasswordUseCase: IResetPasswordUseCase,
    private val logoutUseCase: ILogoutUseCase,
    private val sendVerificationCodeUseCase: ISendVerificationCodeUseCase,
    private val checkVerificationCodeUseCase: ICheckVerificationCodeUseCase
) : ViewModel(), IAuthenticationViewModel {
    private val _authenticationState = MutableStateFlow<UiState<Any>>(UiState.Ideal)
    override val authenticationState: StateFlow<UiState<Any>> get() = _authenticationState.asStateFlow()


    override fun login(loginParams: AuthenticationRequestEntity) {
        authenticateUiState(
            operation = { loginUseCase(loginParams = loginParams) },
            onSuccess = { result ->
                _authenticationState.emit(UiState.Success(result, "login"))

            },
            source = "login"
        )
    }

    override fun signUp(signUpParams: SignUpRequestEntity) {
        authenticateUiState(
            operation = { signUpUseCase(signUpParams = signUpParams) },
            onSuccess = { result ->
                _authenticationState.emit(UiState.Success(result.message, "signUp"))
            },
            source = "signUp"
        )
    }

    override fun resetPassword(resetPasswordParams: EmailRequestEntity) {
        authenticateUiState(
            operation = { resetPasswordUseCase(resetPasswordParams = resetPasswordParams) },
            onSuccess = { result ->
                _authenticationState.emit(UiState.Success(result.message, "resetPassword"))
            },
            source = "resetPassword"
        )
    }

    override fun sendVerificationCode(sendVerificationCodeParams: EmailRequestEntity) {
        authenticateUiState(
            operation = {
                sendVerificationCodeUseCase(
                    sendVerificationCodeParams = sendVerificationCodeParams
                )
            },
            onSuccess = { result ->
                _authenticationState.emit(UiState.Success(result.message, "sendVerificationCode"))
            },
            source = "sendVerificationCode"
        )
    }

    override fun checkVerificationCode(checkVerificationCodeParams: CheckVerificationRequestEntity) {
        authenticateUiState(
            operation = {
                checkVerificationCodeUseCase(
                    checkVerificationCodeParams = checkVerificationCodeParams
                )
            },
            onSuccess = { result ->
                _authenticationState.emit(UiState.Success(result.message, "checkVerificationCode"))

            },
            source = "checkVerificationCode"
        )
    }

    override fun logout() {
        authenticateUiState(
            operation = { logoutUseCase() },
            onSuccess = { result ->
                _authenticationState.emit(UiState.Success(result, "logout"))
            },
            source = "logout"
        )
    }

    override fun <T> authenticateUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    ) {
        viewModelScope.launch {
            _authenticationState.emit(UiState.Loading(source))
            try {
                val result = withContext(Dispatchers.IO) { operation() }
                onSuccess(result)
            } catch (failure: Failures) {
                _authenticationState.emit(UiState.Error(mapFailureMessage(failure), source))
            } catch (e: Exception) {
                _authenticationState.emit(
                    UiState.Error(e.message ?: "Unknown Error", source)
                )
            }

        }
    }

}