package com.example.ecommerce.core.network

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class NetworkHelperViewModel @Inject constructor(
    networkInfo: InternetConnectionChecker
):ViewModel() {
    val networkStatus = networkInfo.statusFlow.asLiveData()
}