package com.example.ecommerce.core.network.checknetwork

import kotlinx.coroutines.flow.StateFlow

interface InternetConnectionChecker {

        val statusFlow: StateFlow<ConnectivityStatus?>
        fun startMonitoring()
        suspend fun hasConnection():Boolean
        suspend fun checkUrls():Boolean
        fun stopMonitoring()

}