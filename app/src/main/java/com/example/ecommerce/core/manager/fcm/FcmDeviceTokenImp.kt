package com.example.ecommerce.core.manager.fcm

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FcmDeviceTokenImp @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : FcmDeviceToken {


    companion object {
        const val FCM_DEVICE_TOKEN: String = "FCM_DEVICE_TOKEN"
    }

    override suspend fun addFcmTokenDevice(token: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putString(FCM_DEVICE_TOKEN, token).apply()
        }

    }

    override suspend fun getFcmTokenDevice(): String? {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getString(FCM_DEVICE_TOKEN, null)
        }


    }

    override suspend fun deleteFcmTokenDevice() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().remove(FCM_DEVICE_TOKEN).apply()
        }

    }
}