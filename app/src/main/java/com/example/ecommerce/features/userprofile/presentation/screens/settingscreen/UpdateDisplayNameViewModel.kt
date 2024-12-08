package com.example.ecommerce.features.userprofile.presentation.screens.settingscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UpdateDisplayNameViewModel:ViewModel() {
    private val _displayName = MutableLiveData<String>()
    val displayName :LiveData<String> get() = _displayName

    fun updateDisplayName(newDisplayName:String){
        _displayName.value = newDisplayName
    }

}