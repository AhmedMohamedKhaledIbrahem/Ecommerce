package com.example.ecommerce.features.address.presentation.viewmodel.addressaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddressActionViewModel : ViewModel(), IAddressActionViewModel {
    private val _addressAction = MutableLiveData<String>()
    override val addressAction: LiveData<String>
        get() = _addressAction

    override fun setAddressAction(action: String) {
        _addressAction.value = action
    }
}