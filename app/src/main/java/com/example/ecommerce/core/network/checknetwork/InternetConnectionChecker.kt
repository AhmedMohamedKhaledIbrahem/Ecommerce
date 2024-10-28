package com.example.ecommerce.core.network.checknetwork

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class InternetConnectionChecker @Inject constructor(
    private val context: Context,
) {
    private val checkInterval: Long = 5000L
    private val checkOptions: List<AddressCheckOption> = defaultCheckOptions
    companion object {
        private val defaultCheckOptions = listOf(
            AddressCheckOption("https://1.1.1.1"),
            AddressCheckOption("https://icanhazip.com"),
            AddressCheckOption("https://jsonplaceholder.typicode.com/todos/1"),
            AddressCheckOption("https://reqres.in/api/users/1")
        )
    }

   private val _statusFlow = MutableStateFlow<ConnectivityStatus?>(null)
    val statusFlow = _statusFlow.asStateFlow()

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    init {
        startMonitoring()
    }

    private fun sendNetworkStatusBroadcast(isConnected: Boolean) {
        val intent = Intent("NETWORK_STATUS")
        intent.putExtra("isConnected", isConnected)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    private fun startMonitoring() {
        scope.launch {
            while (isActive) {
                /**/
                val isConnected = hasConnection()
                withContext(Dispatchers.Main) {
                    _statusFlow.emit(if (isConnected) ConnectivityStatus.CONNECTED else ConnectivityStatus.DISCONNECTED)
                    sendNetworkStatusBroadcast(isConnected = isConnected)
                }
                delay(checkInterval)
            }
        }
    }

    suspend fun hasConnection(): Boolean {
        /* val connectivityManager =
             context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
         return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             val network = connectivityManager.activeNetwork
             val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
             networkCapabilities != null && (
                     networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                             networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                             networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                     )
         } else {
             // For devices below API level 23, use the deprecated methods
             val activeNetworkInfo = connectivityManager.activeNetworkInfo
             activeNetworkInfo != null && activeNetworkInfo.isConnected
         } || checkUrls()*/
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities != null && (
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                ) || checkUrls()

    }

    private suspend fun checkUrls(): Boolean {
        return withContext(Dispatchers.IO) {
            checkOptions.any { option ->
                try {
                    val url = URL(option.url)
                    with(url.openConnection() as HttpURLConnection) {
                        connectTimeout = option.timeout
                        readTimeout = option.timeout
                        requestMethod = "HEAD"
                        responseCode == HttpURLConnection.HTTP_OK
                    }
                } catch (e: Exception) {
                    false
                }
            }
        }
    }

    fun stopMonitoring() {
        job.cancel()
    }

}