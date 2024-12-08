package com.example.ecommerce.features.address.data.datasources.remotedatasource

import com.example.ecommerce.features.address.data.models.AddressRequestModel
import com.example.ecommerce.features.address.data.models.AddressResponseModel
import com.example.ecommerce.features.address.data.models.CheckUpdateAddressResponseModel

interface AddressRemoteDataSource {
    suspend fun updateAddress(updateAddressParams: AddressRequestModel): AddressResponseModel
    suspend fun getAddress(): AddressResponseModel
    suspend fun checkUpdateAddress(): CheckUpdateAddressResponseModel


}