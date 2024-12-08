package com.example.ecommerce.core.utils

import android.os.Build

object EmulatorDevice {
    fun isEmulator(): Boolean {
        // Check for certain device properties that indicate an emulator
        return Build.FINGERPRINT.contains("generic") || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK")
                || Build.PRODUCT.contains("sdk") || Build.PRODUCT.contains("google_sdk")
    }
}