package com.example.ecommerce

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.ecommerce.core.tokenmanager.TokenManager
import com.example.ecommerce.core.utils.PreferencesUtils
import com.example.ecommerce.features.authentication.presentation.screens.loginscreen.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var tokenManager: TokenManager
    @Inject
    lateinit var shardPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        defaultNightMode()
        if (isLogin()) {
            val intent = Intent(this@MainActivity, MainNavigationActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }


    private fun isLogin(): Boolean {
        return !tokenManager.getToken().isNullOrEmpty()
    }

    override fun attachBaseContext(newBase: Context?) {
        val language = PreferencesUtils.languageCode ?: Locale.getDefault().language
        super.attachBaseContext(ContextWrapper(newBase?.setAppLocal(language)))
    }

    private fun Context.setAppLocal(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = this.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return createConfigurationContext(config)
    }

    private fun defaultNightMode() {
        Log.e("defaultNightMode", "defaultNightMode: ${PreferencesUtils.isDarkMode}")
        PreferencesUtils.isDarkMode = shardPreferences.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (PreferencesUtils.isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }


}





