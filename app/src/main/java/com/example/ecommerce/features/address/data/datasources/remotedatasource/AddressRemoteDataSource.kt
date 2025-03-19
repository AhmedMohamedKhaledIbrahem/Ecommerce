package com.example.ecommerce.features.address.data.datasources.remotedatasource

import com.example.ecommerce.features.address.data.models.AddressDataResponseModel
import com.example.ecommerce.features.address.data.models.AddressRequestModel
import com.example.ecommerce.features.address.data.models.UpdateAddressResponseModel
import com.example.ecommerce.features.address.data.models.CheckUpdateAddressResponseModel

interface AddressRemoteDataSource {
    suspend fun updateAddress(updateAddressParams: AddressRequestModel): UpdateAddressResponseModel
    suspend fun getAddress(): AddressDataResponseModel



}