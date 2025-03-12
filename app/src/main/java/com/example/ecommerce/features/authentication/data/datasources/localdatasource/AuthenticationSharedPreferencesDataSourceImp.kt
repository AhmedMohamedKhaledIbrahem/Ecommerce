package com.example.ecommerce.features.authentication.data.datasources.localdatasource

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.manager.token.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationSharedPreferencesDataSourceImp @Inject constructor(
    private val tokenManager: TokenManager
) : AuthenticationSharedPreferencesDataSource {
    override suspend fun saveToken(token: String) {
        withContext(Dispatchers.IO) {
            try {
                tokenManager.saveToken(token = token)
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }

    }

    override suspend fun clearToken() {
        withContext(Dispatchers.IO) {
            try {
                tokenManager.clearToken()
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }
}