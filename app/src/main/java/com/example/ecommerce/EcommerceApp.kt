package com.example.ecommerce

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.hilt.work.HiltWorkerFactory
import com.example.ecommerce.core.constants.Topic
import com.example.ecommerce.core.service.FCMScribe
import com.example.ecommerce.core.service.IFCMScribe
import com.example.ecommerce.core.utils.PreferencesUtils
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class EcommerceApp() : Application() {


    @Inject
    lateinit var shardPreferences: SharedPreferences

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    private val fcmSubscribe: IFCMScribe = FCMScribe
    override fun onCreate() {
        super.onCreate()
        preloadLanguage()
        preloadDarkMode()
        fcmSubscribe.subscribeToTopic(topic = Topic)
    }


    fun hideSystemUI(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            activity.window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            activity.window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
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

    private fun isDarkModeEnabled(): Boolean {
        return shardPreferences.getBoolean("dark_mode", false)
    }

}