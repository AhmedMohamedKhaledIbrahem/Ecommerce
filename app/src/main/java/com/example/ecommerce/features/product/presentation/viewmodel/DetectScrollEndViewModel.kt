package com.example.ecommerce.features.product.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetectScrollEndViewModel : ViewModel() {
    private val _detectScroll = MutableLiveData<Boolean>()
    val detectScroll: LiveData<Boolean> get() = _detectScroll

    fun updateDetectScroll(detect: Boolean) {
        _detectScroll.value = detect
    }
}