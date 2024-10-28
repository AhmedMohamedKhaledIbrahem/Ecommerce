package com.example.ecommerce.core.tokenmanager

import android.content.SharedPreferences
import javax.inject.Inject

class TokenManagerImp @Inject constructor(
    private val preferences:SharedPreferences
):TokenManager {
    companion object{
        private const val TOKEN_KEY = "auth_token"
    }
    override fun saveToken(token: String) {
       preferences.edit().putString(TOKEN_KEY,token).apply()
    }

    override fun getToken(): String? {
       return preferences.getString(TOKEN_KEY,null)
    }

    override fun clearToken() {
        preferences.edit().remove(TOKEN_KEY).apply()
    }
}