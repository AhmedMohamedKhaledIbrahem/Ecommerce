package com.example.ecommerce.features.address.data.datasources

import com.example.ecommerce.features.address.data.models.AddressDataResponseModel
import com.example.ecommerce.features.address.data.models.AddressRequestModel
import com.example.ecommerce.features.address.data.models.UpdateAddressResponseModel
import com.example.ecommerce.features.address.data.models.CheckUpdateAddressResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface AddressApi {
    @PUT("wp-json/custom/v1/customer-address")
    suspend fun updateAddress(
        @Body updateAddressParams: AddressRequestModel
    ): Response<UpdateAddressResponseModel>

    @GET("wp-json/custom/v1/customer-info")
    suspend fun getAddress(): Response<AddressDataResponseModel>



}