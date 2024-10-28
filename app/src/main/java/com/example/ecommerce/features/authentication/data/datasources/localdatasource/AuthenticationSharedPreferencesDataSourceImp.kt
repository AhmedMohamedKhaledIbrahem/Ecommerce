package com.example.ecommerce.features.authentication.data.datasources.localdatasource

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.tokenmanager.TokenManager
import javax.inject.Inject

class AuthenticationSharedPreferencesDataSourceImp @Inject constructor(
  private  val tokenManager: TokenManager
) :AuthenticationSharedPreferencesDataSource{
    override suspend fun saveToken(token: String) {
        try {
            tokenManager.saveToken(token = token)
        }catch (e:Exception){
            throw FailureException("${e.message}")
        }

    }

    override suspend fun clearToken() {
        try {
            tokenManager.clearToken()
        }catch (e:Exception){
            throw FailureException("${e.message}")
        }
    }
}