package com.example.ecommerce.core.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.ecommerce.MainActivity
import com.example.ecommerce.R
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.network.checknetwork.ConnectivityStatus



fun checkInternetConnection(
    lifecycleOwner: LifecycleOwner,
    networkStatus: LiveData<ConnectivityStatus?>,
    activity: MainActivity
):Boolean {
    val rootView = activity.binding.root
    //val loadingDialog = activity.loadingDialog
    val context = activity.applicationContext
   val fragmentManager = activity.supportFragmentManager
    var isDisconnected = false
    networkStatus.observe(lifecycleOwner) { connectivityStatus ->
        when (connectivityStatus) {
            ConnectivityStatus.CONNECTED -> {
                if (isDisconnected) {
                    LoadingDialogFragment.dismiss(fragmentManager = fragmentManager)
                    SnackBarCustom.showSnackbar(
                        rootView,
                        context.getString(R.string.your_internet_has_been_restored),
                        R.drawable.baseline_wifi_24
                    )
                    isDisconnected = false
                }
            }
            ConnectivityStatus.DISCONNECTED -> {
                LoadingDialogFragment.show(fragmentManager = fragmentManager)
                SnackBarCustom.showSnackbar(
                    rootView,
                    context.getString(R.string.you_are_currently_offline),
                    R.drawable.baseline_wifi_off_24
                )
                isDisconnected = true
            }
            null -> Unit
        }
    }
    return isDisconnected
}

