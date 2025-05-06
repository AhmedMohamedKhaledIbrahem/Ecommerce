package com.example.ecommerce.features.address.presentation.viewmodel.addressaction

import androidx.lifecycle.LiveData

interface IAddressActionViewModel {
    val addressAction : LiveData<String>
    fun setAddressAction(action: String)
}