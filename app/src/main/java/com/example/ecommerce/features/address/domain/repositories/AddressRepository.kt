package com.example.ecommerce.features.address.domain.repositories

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity

interface AddressRepository {
    suspend fun updateAddress(customerAddressParams: AddressRequestEntity)
    suspend fun getAddressById(id: Int): CustomerAddressEntity
    suspend fun checkUpdateAddress(): CustomerAddressEntity
}