package com.example.ecommerce.core.manager.address

interface AddressManager {
    suspend fun getAddressId(): Int
    suspend fun setAddressId(addressId: Int)
}