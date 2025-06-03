package com.example.ecommerce.features.logout.data.source.remote

import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.constants.status
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.logout.data.source.LogoutApi
import javax.inject.Inject

interface LogoutRemoteDataSource {

    suspend fun removeFcmToken(tokenParams: String)
}

class LogoutRemoteDataSourceImp @Inject constructor(
    private val api: LogoutApi
) : LogoutRemoteDataSource {

    override suspend fun removeFcmToken(tokenParams: String) {
        try {
            val response = api.removeFcmToken(token = tokenParams)
            if (!response.isSuccessful) {
                throw FailureException("$status:${response.code()}")
            }
        } catch (e: Exception) {
            throw FailureException(e.message ?: Unknown_Error)
        }
    }

}