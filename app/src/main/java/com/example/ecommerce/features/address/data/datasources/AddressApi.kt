package com.example.ecommerce.features.address.data.datasources

import com.example.ecommerce.core.constants.CUSTOMER_ADDRESS_END_POINT
import com.example.ecommerce.core.constants.CUSTOMER_INFO_END_POINT
import com.example.ecommerce.features.address.data.models.AddressDataResponseModel
import com.example.ecommerce.features.address.data.models.AddressRequestModel
import com.example.ecommerce.features.address.data.models.UpdateAddressResponseModel
import com.example.ecommerce.features.address.data.models.CheckUpdateAddressResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface AddressApi {
    @PUT(CUSTOMER_ADDRESS_END_POINT)
    suspend fun updateAddress(
        @Body updateAddressParams: AddressRequestModel
    ): Response<UpdateAddressResponseModel>

    @GET(CUSTOMER_INFO_END_POINT)
    suspend fun getAddress(): Response<AddressDataResponseModel>



}