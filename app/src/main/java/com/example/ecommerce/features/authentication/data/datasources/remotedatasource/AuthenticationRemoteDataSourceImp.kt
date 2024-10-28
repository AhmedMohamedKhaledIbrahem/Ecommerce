package com.example.ecommerce.features.authentication.data.datasources.remotedatasource

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.authentication.data.datasources.AuthenticationApi
import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel
import com.example.ecommerce.features.authentication.data.models.MessageResponseModel
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
                    throw FailureException("Server Error: ${response.message()}")
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
                    throw FailureException("Server Error: ${response.message()}")
                }
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }

    override suspend fun resetPassword(email: String): MessageResponseModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.resetPasswordRequest(email = email)
                if (response.isSuccessful) {
                    response.body() ?: throw FailureException("Empty Response Body")
                } else {
                    throw FailureException("Server Error: ${response.message()}")
                }
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }
}