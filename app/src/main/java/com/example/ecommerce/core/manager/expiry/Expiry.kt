package com.example.ecommerce.core.manager.expiry

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface Expiry {
    suspend fun setEnableLogout(enable: Boolean)
    fun getEnableLogout(): Boolean
    fun clearEnableLogout()
    suspend fun setExpiryTime(time: Long)
    fun getExpiryTime(): Long
    fun clearExpiryTime()

}

class ExpiryImp @Inject constructor(
    private val preferences: SharedPreferences
) : Expiry {
    override suspend fun setEnableLogout(enable: Boolean) {
        try {
            withContext(Dispatchers.IO) {
                preferences.edit { putBoolean(ENABLE_LOGOUT_KEY, enable) }
            }
        } catch (e: Exception) {
            Log.d("TAG", "setEnableLogout: ${e.message}")
        }
    }

    override fun getEnableLogout(): Boolean {
        return try {
            preferences.getBoolean(ENABLE_LOGOUT_KEY, false)
        } catch (e: Exception) {
            Log.d("TAG", "setEnableLogout: ${e.message}")
            false
        }
    }

    override fun clearEnableLogout() {
        try {
            preferences.edit { remove(ENABLE_LOGOUT_KEY) }
        } catch (e: Exception) {
            Log.d("TAG", "setEnableLogout: ${e.message}")
        }
    }

    override suspend fun setExpiryTime(time: Long) {
        try {
            withContext(Dispatchers.IO) {
                preferences.edit { putLong(EXPIRY_TIME_KEY, time) }
            }
        } catch (e: Exception) {
            Log.d("TAG", "setEnableLogout: ${e.message}")
        }
    }

    override fun getExpiryTime(): Long {
        return try {
            preferences.getLong(EXPIRY_TIME_KEY, 0L)
        } catch (e: Exception) {
            Log.d("TAG", "setEnableLogout: ${e.message}")
            0L
        }
    }

    override fun clearExpiryTime() {
        try {
            preferences.edit { remove(EXPIRY_TIME_KEY) }
        } catch (e: Exception) {
            Log.d("TAG", "setEnableLogout: ${e.message}")
        }
    }

    companion object {
        private const val ENABLE_LOGOUT_KEY = "enable_logout"
        private const val EXPIRY_TIME_KEY = "expiry_time"

    }

}