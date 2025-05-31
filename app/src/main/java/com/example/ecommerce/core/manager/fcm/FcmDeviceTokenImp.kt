package com.example.ecommerce.core.manager.fcm

import android.content.SharedPreferences
import androidx.core.content.edit
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
            sharedPreferences.edit() { putString(FCM_DEVICE_TOKEN, token) }
        }

    }

    override fun getFcmTokenDevice(): String? {
        return sharedPreferences.getString(FCM_DEVICE_TOKEN, null)
    }

    override suspend fun deleteFcmTokenDevice() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit() { remove(FCM_DEVICE_TOKEN) }
        }

    }
}