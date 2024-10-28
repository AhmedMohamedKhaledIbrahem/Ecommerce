package com.example.ecommerce.features.authentication.data.datasources.localdatasource

interface AuthenticationSharedPreferencesDataSource {
   suspend fun saveToken(token:String)
   suspend fun clearToken()
}