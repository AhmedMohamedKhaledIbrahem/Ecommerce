package com.example.ecommerce.core.app

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.ecommerce.core.network.NetworkHelperViewModel
import com.example.ecommerce.core.utils.NetworkStatus


class AppLifecycleObserver(
    private val networkStatusViewModel: NetworkHelperViewModel,
    private val appLifecycleViewModel: AppLifecycleViewModel,
) :
    DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

    }

    override fun onStart(owner: LifecycleOwner) {
        appLifecycleViewModel.rootView?.let {
            appLifecycleViewModel.loadingDialog?.let { it1 ->
                appLifecycleViewModel.fragmentManager?.let { it2 ->
                    NetworkStatus.checkInternetConnection(
                        lifecycleOwner = owner,
                        networkStatus = networkStatusViewModel.networkStatus,
                        loadingDialog = it1,
                        fragmentManager = it2,
                        rootView = it
                    )
                }
            }
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        appLifecycleViewModel.rootView?.let {
            appLifecycleViewModel.loadingDialog?.let { it1 ->
                appLifecycleViewModel.fragmentManager?.let { it2 ->
                    NetworkStatus.checkInternetConnection(
                        lifecycleOwner = owner,
                        networkStatus = networkStatusViewModel.networkStatus,
                        loadingDialog = it1,
                        fragmentManager = it2,
                        rootView = it
                    )
                }
            }
        }

    }

}