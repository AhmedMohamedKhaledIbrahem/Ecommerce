package com.example.ecommerce.core.tokenmanager

interface TokenManager {
    fun saveToken(token:String)
    fun getToken():String?
    fun clearToken()
}