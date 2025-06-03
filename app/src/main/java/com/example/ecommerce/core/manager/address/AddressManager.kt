package com.example.ecommerce.core.manager.address

interface AddressManager {
    suspend fun getAddressId(): Int
    suspend fun setAddressId(addressId: Int)
    suspend fun clearAddressId()
    suspend fun enableFetchAddress(enable: Boolean)
    fun getFetchAddress(): Boolean
    suspend fun clearFetchAddress()
}