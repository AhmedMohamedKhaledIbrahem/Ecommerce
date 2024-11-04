package com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel

import androidx.lifecycle.LiveData
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.CheckVerificationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.features.authentication.presentation.viewmodel.state.UiState

interface IAuthenticationViewModel {
    val authenticationState: LiveData<UiState<Any>>
    fun login(loginParams: AuthenticationRequestEntity)
    fun signUp(signUpParams: SignUpRequestEntity)
    fun resetPassword(resetPasswordParams: EmailRequestEntity)
    fun sendVerificationCode(sendVerificationCodeParams:EmailRequestEntity)
    fun checkVerificationCode(checkVerificationCodeParams: CheckVerificationRequestEntity)
    fun logout()
    fun <T> authenticate(
        operation: suspend () -> T,
        onSuccess: (T) -> Unit
    )
}