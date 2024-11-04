package com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.CheckVerificationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.features.authentication.domain.usecases.checkverificationcode.ICheckVerificationCodeUseCase
import com.example.ecommerce.features.authentication.domain.usecases.login.ILoginUseCase
import com.example.ecommerce.features.authentication.domain.usecases.logout.ILogoutUseCase
import com.example.ecommerce.features.authentication.domain.usecases.reestpassword.IResetPasswordUseCase
import com.example.ecommerce.features.authentication.domain.usecases.sendverificationcode.ISendVerificationCodeUseCase
import com.example.ecommerce.features.authentication.domain.usecases.signup.ISignUpUseCase
import com.example.ecommerce.features.authentication.presentation.viewmodel.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val _authenticationState = MutableLiveData<UiState<Any>>()
    override val authenticationState: LiveData<UiState<Any>> get() = _authenticationState


    override fun login(loginParams: AuthenticationRequestEntity) {
        authenticate(
            operation = { loginUseCase(loginParams = loginParams) },
            onSuccess = { result ->
                _authenticationState.value = UiState.Success(result)
            }
        )
    }

    override fun signUp(signUpParams: SignUpRequestEntity) {
        authenticate(
            operation = { signUpUseCase(signUpParams = signUpParams) },
            onSuccess = { result ->

                _authenticationState.value = UiState.Success(result.message)
            }
        )
    }

    override fun resetPassword(resetPasswordParams: EmailRequestEntity) {
        authenticate(
            operation = { resetPasswordUseCase(resetPasswordParams = resetPasswordParams) },
            onSuccess = { result ->
                _authenticationState.value = UiState.Success(result.message)
            }
        )
    }

    override fun sendVerificationCode(sendVerificationCodeParams: EmailRequestEntity) {
        authenticate(
            operation = {
                sendVerificationCodeUseCase(
                    sendVerificationCodeParams = sendVerificationCodeParams
                )
            },
            onSuccess = { result ->
                _authenticationState.value = UiState.Success(result.message)
            }
        )
    }

    override fun checkVerificationCode(checkVerificationCodeParams: CheckVerificationRequestEntity) {
        authenticate(
            operation = {
                checkVerificationCodeUseCase(
                    checkVerificationCodeParams = checkVerificationCodeParams
                )
            },
            onSuccess = { result ->
                _authenticationState.value = UiState.Success(result.message)
            }
        )
    }

    override fun logout() {
        authenticate(
            operation = { logoutUseCase() },
            onSuccess = { result ->
                _authenticationState.value = UiState.Success(result)
            }
        )
    }

    override fun <T> authenticate(
        operation: suspend () -> T,
        onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
            _authenticationState.value = UiState.Loading
            try {
                val result = withContext(Dispatchers.IO) { operation() }
                onSuccess(result)
            } catch (failure: Failures) {
                _authenticationState.value =
                    UiState.Error(mapFailureMessage(failure))
            } catch (e: Exception) {
                _authenticationState.value =
                    UiState.Error(e.message ?: "Unknown Error")
            }

        }
    }

}