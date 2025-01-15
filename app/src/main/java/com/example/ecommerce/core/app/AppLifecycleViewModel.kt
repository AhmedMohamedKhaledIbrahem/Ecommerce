package com.example.ecommerce.core.app

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppLifecycleViewModel @Inject constructor() :ViewModel(){
    @SuppressLint("StaticFieldLeak")
    var rootView: View? = null
    var loadingDialog: LoadingDialogFragment? = null
    var fragmentManager: FragmentManager? = null
}