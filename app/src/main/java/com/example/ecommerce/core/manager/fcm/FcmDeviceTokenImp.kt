package com.example.ecommerce.core.manager.fcm

import android.content.SharedPreferences
import javax.inject.Inject

class FcmDeviceTokenImp @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : FcmDeviceToken {


    companion object {
        const val FCM_DEVICE_TOKEN: String = "FCM_DEVICE_TOKEN"
    }

    override suspend fun addFcmTokenDevice(token: String) {
        sharedPreferences.edit().putString(FCM_DEVICE_TOKEN, token).apply()
    }

    override suspend fun getFcmTokenDevice(): String? {
        return sharedPreferences.getString(FCM_DEVICE_TOKEN, null)

    }

    override suspend fun deleteFcmTokenDevice() {
        sharedPreferences.edit().remove(FCM_DEVICE_TOKEN).apply()
    }
}