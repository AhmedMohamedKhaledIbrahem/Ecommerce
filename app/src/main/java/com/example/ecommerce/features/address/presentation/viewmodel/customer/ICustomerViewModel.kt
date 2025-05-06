package com.example.ecommerce.features.address.presentation.viewmodel.customer

import androidx.lifecycle.LiveData
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity

interface ICustomerViewModel {
    val customerEntity :LiveData<CustomerAddressEntity>
    fun setCustomerEntity(customerEntity: CustomerAddressEntity)

}