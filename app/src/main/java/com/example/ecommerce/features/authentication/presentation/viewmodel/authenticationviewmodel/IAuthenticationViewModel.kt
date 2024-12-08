package com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel

import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.CheckVerificationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.core.state.UiState
import kotlinx.coroutines.flow.StateFlow

interface IAuthenticationViewModel {
    val authenticationState: StateFlow<UiState<Any>>
    fun login(loginParams: AuthenticationRequestEntity)
    fun signUp(signUpParams: SignUpRequestEntity)
    fun resetPassword(resetPasswordParams: EmailRequestEntity)
    fun sendVerificationCode(sendVerificationCodeParams: EmailRequestEntity)
    fun checkVerificationCode(checkVerificationCodeParams: CheckVerificationRequestEntity)
    fun logout()
    fun <T> authenticateUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    )
}