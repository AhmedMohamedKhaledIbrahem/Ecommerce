package com.example.ecommerce.core.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NetworkStatuesHelperViewModel @Inject constructor(
    private val networkInfo: InternetConnectionChecker
) : ViewModel() {
    val networkStatus = networkInfo.statusFlow.asLiveData()
}