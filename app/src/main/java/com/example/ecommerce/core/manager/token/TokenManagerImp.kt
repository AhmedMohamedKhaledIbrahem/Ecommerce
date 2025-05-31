package com.example.ecommerce.core.manager.token

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokenManagerImp @Inject constructor(
    private val preferences: SharedPreferences
) : TokenManager {
    companion object {
        private const val TOKEN_KEY = "auth_token"
    }

    override suspend fun saveToken(token: String) {
        withContext(Dispatchers.IO) {
            preferences.edit() { putString(TOKEN_KEY, token) }
        }

    }

    override fun getToken(): String? {
        return preferences.getString(TOKEN_KEY, null)
    }

    override suspend fun clearToken() {
        withContext(Dispatchers.IO) {
            preferences.edit() { remove(TOKEN_KEY) }
        }
    }
}