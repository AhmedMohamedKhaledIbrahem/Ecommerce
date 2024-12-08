package com.example.ecommerce.features.address.data.datasources.localdatasource

import com.example.ecommerce.core.data.entities.CustomerAddressEntity
import com.example.ecommerce.features.address.data.models.AddressResponseModel

interface AddressLocalDataSource {
    suspend fun updateAddress(updateAddressParams: AddressResponseModel)
    suspend fun getAddressById(id:Int): CustomerAddressEntity
    suspend fun checkAddressEntityById(id: Int): CustomerAddressEntity?
}