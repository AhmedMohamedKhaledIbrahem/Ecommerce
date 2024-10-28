package com.example.ecommerce.features.authentication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.features.authentication.domain.usecases.LoginUseCase
import com.example.ecommerce.features.authentication.domain.usecases.LogoutUseCase
import com.example.ecommerce.features.authentication.domain.usecases.ResetPasswordUseCase
import com.example.ecommerce.features.authentication.domain.usecases.SignUpUseCase
import com.example.ecommerce.features.authentication.presentation.viewmodel.state.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val networkInfo: InternetConnectionChecker,
) : ViewModel() {
    private val _authenticationState = MutableLiveData<AuthenticationState<Any>>()
    val authenticationState: LiveData<AuthenticationState<Any>> get() = _authenticationState
    //val networkStatus = networkInfo.statusFlow.asLiveData()

    fun login(loginParams: AuthenticationRequestEntity) {
        authentication(
            operation = { loginUseCase(loginParams = loginParams) },
            onSuccess = { result ->
                _authenticationState.value = AuthenticationState.Success(result)
            }
        )
    }

    fun signUp(signUpParams: SignUpRequestEntity) {
        authentication(
            operation = { signUpUseCase(signUpParams = signUpParams) },
            onSuccess = { result ->
                _authenticationState.value = AuthenticationState.Success(result)
            }
        )
    }

    fun resetPassword(email: String) {
        authentication(
            operation = { resetPasswordUseCase(email = email) },
            onSuccess = { result ->
                _authenticationState.value = AuthenticationState.Success(result)
            }
        )
    }

    fun logout() {
        authentication(
            operation = { logoutUseCase.invoke() },
            onSuccess = { result ->
                _authenticationState.value = AuthenticationState.Success(result)
            }
        )
    }

    private fun <T> authentication(
        operation: suspend () -> T,
        onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
            _authenticationState.value = AuthenticationState.Loading
            /* val isConnected = withContext(Dispatchers.IO) {
                 networkInfo.hasConnection()
             }
             if (isConnected) {}*/
            try {
                val result = withContext(Dispatchers.IO) { operation() }
                onSuccess(result)
            } catch (failure: Failures) {
                _authenticationState.value =
                    AuthenticationState.Error(mapFailureMessage(failure))
            } catch (e: Exception) {
                _authenticationState.value =
                    AuthenticationState.Error(e.message ?: "Unknown Error")
            }

        }
    }

}