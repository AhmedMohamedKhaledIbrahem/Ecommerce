package com.example.ecommerce.features.authentication.data.datasources

import com.example.ecommerce.core.constants.CHANGE_PASSWORD_END_POINT
import com.example.ecommerce.core.constants.CHECK_VERIFICATION_STATUS_END_POINT
import com.example.ecommerce.core.constants.CONFIRM_PASSWORD_RESET_END_POINT
import com.example.ecommerce.core.constants.RESET_PASSWORD_END_POINT
import com.example.ecommerce.core.constants.SEND_VERIFICATION_CODE_END_POINT
import com.example.ecommerce.core.constants.SIGN_UP_END_POINT
import com.example.ecommerce.core.constants.TOKEN_END_POINT
import com.example.ecommerce.features.authentication.data.models.AuthenticationRequestModel
import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel
import com.example.ecommerce.features.authentication.data.models.ChangePasswordRequestModel
import com.example.ecommerce.features.authentication.data.models.CheckVerificationRequestModel
import com.example.ecommerce.features.authentication.data.models.ConfirmPasswordResetRequestModel
import com.example.ecommerce.features.authentication.data.models.EmailRequestModel
import com.example.ecommerce.features.authentication.data.models.MessageResponseModel
import com.example.ecommerce.features.authentication.data.models.SignUpRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {
    @POST(TOKEN_END_POINT)
    suspend fun loginRequest(@Body request: AuthenticationRequestModel): Response<AuthenticationResponseModel>

    @POST(SIGN_UP_END_POINT)
    suspend fun signUpRequest(@Body request: SignUpRequestModel): Response<MessageResponseModel>

    @POST(RESET_PASSWORD_END_POINT)
    suspend fun resetPasswordRequest(@Body request: EmailRequestModel): Response<MessageResponseModel>

    @POST(SEND_VERIFICATION_CODE_END_POINT)
    suspend fun sendVerificationCodeRequest(@Body request: EmailRequestModel): Response<MessageResponseModel>

    @POST(CHECK_VERIFICATION_STATUS_END_POINT)
    suspend fun checkVerificationCodeRequest(@Body request: CheckVerificationRequestModel): Response<MessageResponseModel>

    @POST(CHANGE_PASSWORD_END_POINT)
    suspend fun changePasswordRequest(@Body request: ChangePasswordRequestModel): Response<MessageResponseModel>

    @POST(CONFIRM_PASSWORD_RESET_END_POINT)
    suspend fun confirmPasswordReset(@Body request: ConfirmPasswordResetRequestModel): Response<MessageResponseModel>

}