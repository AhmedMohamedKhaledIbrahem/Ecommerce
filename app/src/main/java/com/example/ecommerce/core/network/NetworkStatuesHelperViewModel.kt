package com.example.ecommerce.core.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.core.network.checknetwork.InternetConnectionCheckerImp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NetworkStatuesHelperViewModel @Inject constructor(
    networkInfo: InternetConnectionChecker
) : ViewModel() {
    val networkStatus = networkInfo.statusFlow.asLiveData()
}