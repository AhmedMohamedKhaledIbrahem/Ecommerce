package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce.core.tokenmanager.TokenManager
import com.example.ecommerce.features.authentication.presentation.screens.loginscreen.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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


}





