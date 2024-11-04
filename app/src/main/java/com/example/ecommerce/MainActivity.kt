package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce.features.authentication.presentation.screens.CheckVerificationCodeActivity
import com.example.ecommerce.features.authentication.presentation.screens.LoginActivity
import com.example.ecommerce.features.authentication.presentation.screens.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)

    }


}





