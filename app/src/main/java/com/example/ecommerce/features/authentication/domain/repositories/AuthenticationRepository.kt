package com.example.ecommerce.features.authentication.domain.repositories

import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.ChangePasswordRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.CheckVerificationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.ConfirmPasswordResetRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity

interface AuthenticationRepository {
    suspend fun login(loginParams: AuthenticationRequestEntity): AuthenticationResponseEntity
    suspend fun signUp(singUpParams: SignUpRequestEntity): MessageResponseEntity
    suspend fun resetPassword(resetPasswordParams: EmailRequestEntity): MessageResponseEntity
    suspend fun sendVerificationCode(sendVerificationCodeParams: EmailRequestEntity): MessageResponseEntity
    suspend fun checkVerificationCode(checkVerificationCodeParams: CheckVerificationRequestEntity): MessageResponseEntity
    suspend fun confirmPasswordChange(confirmPasswordChangeParams: ConfirmPasswordResetRequestEntity): MessageResponseEntity
    suspend fun changePassword(changePasswordParams: ChangePasswordRequestEntity): MessageResponseEntity

}