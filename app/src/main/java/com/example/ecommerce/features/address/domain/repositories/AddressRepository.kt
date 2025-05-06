package com.example.ecommerce.features.address.domain.repositories

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.entites.UpdateAddressResponseEntity

interface AddressRepository {
    suspend fun updateAddress(id: Int, customerAddressParams: AddressRequestEntity): UpdateAddressResponseEntity
    suspend fun getAddress(): List<CustomerAddressEntity>
    suspend fun insertAddress(customerAddressParams: AddressRequestEntity)
    suspend fun deleteAllAddress()
    suspend fun deleteAddress(customerAddressEntity: CustomerAddressEntity)
    suspend fun getSelectAddress(customerId: Int): CustomerAddressEntity
    suspend fun unSelectAddress(customerId: Int)
    suspend fun selectAddress(customerId: Int)


}