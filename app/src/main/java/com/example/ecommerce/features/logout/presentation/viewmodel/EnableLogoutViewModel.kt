package com.example.ecommerce.features.logout.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class EnableLogoutViewModel : ViewModel() {
    private val _enableLogout = MutableLiveData<Boolean>()
    val enableLogout: LiveData<Boolean> get() = _enableLogout

    fun setEnableLogout(enable: Boolean) {
        _enableLogout.postValue(enable)
    }
}