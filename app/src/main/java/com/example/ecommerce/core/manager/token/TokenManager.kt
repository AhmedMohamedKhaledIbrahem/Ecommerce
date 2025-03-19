package com.example.ecommerce.core.manager.token

interface TokenManager {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
}