package com.example.ecommerce.core.manager.token

interface TokenManager {
    fun saveToken(token:String)
    fun getToken():String?
    fun clearToken()
}