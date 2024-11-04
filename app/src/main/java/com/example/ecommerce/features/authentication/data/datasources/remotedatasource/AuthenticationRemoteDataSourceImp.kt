package com.example.ecommerce.features.authentication.data.datasources.remotedatasource

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.authentication.data.datasources.AuthenticationApi
import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel
import com.example.ecommerce.features.authentication.data.models.MessageResponseModel
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.CheckVerificationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

class AuthenticationRemoteDataSourceImp @Inject constructor(
    private val api: AuthenticationApi
) : AuthenticationRemoteDataSource {

    override suspend fun login(loginParams: AuthenticationRequestEntity): AuthenticationResponseModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.loginRequest(request = loginParams)
                if (response.isSuccessful) {
                    response.body() ?: throw FailureException("Empty Response Body")
                } else {
                    val errorBody = response.errorBody()?.string()

                    val errorMessage = errorBody?.let {
                        JSONObject(it).optString("message", "Unknown error")
                    } ?: "Unknown error"
                    val cleanedErrorMessage = errorMessage
                        .replace("Error", "")
                        .replace(":", "")
                        .replace(Regex("<a[^>]*>[^<]*</a>"), "")
                        .replace("\\s+|<strong>|</strong>".toRegex(), " ")
                        .replace(Regex("\\.[^.]*\\."), "")
                        .trim()

                    throw FailureException(cleanedErrorMessage)
                }
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }

    override suspend fun signUp(signUpParams: SignUpRequestEntity): MessageResponseModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.signUpRequest(request = signUpParams)
                if (response.isSuccessful) {
                    response.body() ?: throw FailureException("Empty Response Body")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        JSONObject(it).optString("message", "Unknown error")
                    } ?: "Unknown error"
                    throw FailureException(errorMessage)
                }
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }

    override suspend fun resetPassword(resetPasswordParams: EmailRequestEntity): MessageResponseModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.resetPasswordRequest(request = resetPasswordParams)
                if (response.isSuccessful) {
                    response.body() ?: throw FailureException("Empty Response Body")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        JSONObject(it).optString("message", "Unknown error")
                    } ?: "Unknown error"
                    throw FailureException(errorMessage)
                }
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }

    override suspend fun sendVerificationCode(sendVerificationCodeParams: EmailRequestEntity)
            : MessageResponseModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.sendVerificationCodeRequest(request = sendVerificationCodeParams)
                if (response.isSuccessful) {
                    response.body() ?: throw FailureException("Empty Response Body")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        JSONObject(it).optString("message", "Unknown error")
                    } ?: "Unknown error"
                    throw FailureException(errorMessage)
                }
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }

    override suspend fun checkVerificationCode(
        checkVerificationCodeParams: CheckVerificationRequestEntity
    ): MessageResponseModel {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    api.checkVerificationCodeRequest(request = checkVerificationCodeParams)
                if (response.isSuccessful) {
                    response.body() ?: throw FailureException("Empty Response Body")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        JSONObject(it).optString("message", "Unknown error")
                    } ?: "Unknown error"
                    throw FailureException(errorMessage)
                }
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }
}