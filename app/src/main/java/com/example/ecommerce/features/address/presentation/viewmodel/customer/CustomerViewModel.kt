package com.example.ecommerce.features.address.presentation.viewmodel.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity

class CustomerViewModel() : ViewModel(), ICustomerViewModel {
    private val _customerEntity = MutableLiveData<CustomerAddressEntity>()
    override val customerEntity: LiveData<CustomerAddressEntity> get() = _customerEntity
    override fun setCustomerEntity(customerEntity: CustomerAddressEntity) {
        _customerEntity.value = customerEntity
    }
}