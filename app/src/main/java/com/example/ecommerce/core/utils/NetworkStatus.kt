package com.example.ecommerce.core.utils

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.ecommerce.R
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.network.checknetwork.ConnectivityStatus

object NetworkStatus {
    fun checkInternetConnection(
        lifecycleOwner: LifecycleOwner,
        networkStatus: LiveData<ConnectivityStatus?>,
        loadingDialog: LoadingDialogFragment,
        fragmentManager: FragmentManager,
        rootView: View
    ):Boolean {
        var flag = false
        networkStatus.observe(lifecycleOwner) { connectivityStatus ->
            when (connectivityStatus) {
                ConnectivityStatus.CONNECTED -> {
                    if (flag) {
                        loadingDialog.dismissLoading()
                        SnackBarCustom.showSnackbar(
                            rootView,
                            "Your Internet has been Restored",
                            R.drawable.baseline_wifi_24
                        )
                        flag = false
                    }
                }
                ConnectivityStatus.DISCONNECTED -> {
                    loadingDialog.showLoading(fragmentManager)
                    SnackBarCustom.showSnackbar(
                        rootView,
                        "You are currently offline",
                        R.drawable.baseline_wifi_off_24
                    )
                    flag = true
                }
                null -> {}
            }
        }
        return flag
    }
}