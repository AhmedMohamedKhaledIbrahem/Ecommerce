package com.example.ecommerce.features.address.data.datasources.localdatasource

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.features.address.data.models.AddressRequestModel

interface AddressLocalDataSource {
    suspend fun updateAddress(id:Int,addressRequestParams: AddressRequestModel)
    suspend fun insertAddress(addressRequestParams: AddressRequestModel)
    suspend fun getAddress(): List<CustomerAddressEntity>
    suspend fun isAddressEmpty(): Boolean
    suspend fun deleteAddress()

}