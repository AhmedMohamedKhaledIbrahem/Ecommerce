package com.example.ecommerce.features.authentication.presentation.screens

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleOwner
import com.example.ecommerce.R
import com.example.ecommerce.core.network.NetworkStatuesHelperViewModel
import com.example.ecommerce.core.network.checknetwork.ConnectivityStatus
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
   // @Inject
    //lateinit var networkStatusHelper: NetworkStatusHelper
   private val networkStatusViewModel: NetworkStatuesHelperViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        networkStatusViewModel.networkStatus.observe(this){connectivityStatus  ->
            when(connectivityStatus){
                ConnectivityStatus.CONNECTED -> {
                    Toast.makeText(this,"now CONNECTED",Toast.LENGTH_SHORT).show()
                }
                ConnectivityStatus.DISCONNECTED -> {
                    Toast.makeText(this,"now is not CONNECTED",Toast.LENGTH_SHORT).show()
                }
                null ->{}
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver
       // networkStatusHelper.unRegisterReceiver()
    }
}