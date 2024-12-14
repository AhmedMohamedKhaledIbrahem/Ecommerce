package com.example.ecommerce

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import com.example.ecommerce.core.utils.PreferencesUtils
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class EcommerceApp : Application() {
    @Inject
    lateinit var shardPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        preloadLanguage()
        preloadDarkMode()
    }

    fun hideSystemUI(activity: Activity) {
        activity.window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

    private fun preloadLanguage() {
        val languageCode = getStoredLanguage() ?: Locale.getDefault().language
        PreferencesUtils.languageCode = languageCode
    }
    private fun preloadDarkMode() {
        val darkMode = isDarkModeEnabled()
        PreferencesUtils.isDarkMode = darkMode
    }

    private fun getStoredLanguage(): String? {
        return shardPreferences.getString("language", null)
    }
    private fun isDarkModeEnabled(): Boolean{
        return shardPreferences.getBoolean("dark_mode", false)
    }

}