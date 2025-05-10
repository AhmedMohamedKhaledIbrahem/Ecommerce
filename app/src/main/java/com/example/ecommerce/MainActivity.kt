package com.example.ecommerce

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.ecommerce.core.manager.token.TokenManager
import com.example.ecommerce.MainNavigationActivity
import com.example.ecommerce.core.utils.PreferencesUtils
import com.example.ecommerce.databinding.ActivityMainBinding
import com.example.ecommerce.features.authentication.presentation.screens.loginscreen.LoginFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

//@AndroidEntryPoint
//class MainActivity : AppCompatActivity() {
//    @Inject
//    lateinit var tokenManager: TokenManager
//
//    @Inject
//    lateinit var shardPreferences: SharedPreferences
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        lifecycleScope.launch {
//            when (isLogin()) {
//                true -> {
//                    val intent = Intent(this@MainActivity, MainNavigationActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//                false -> {
//                    val intent = Intent(this@MainActivity, LoginFragment::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            }
//        }
//
//
//    }
//
//
//
//
//}





