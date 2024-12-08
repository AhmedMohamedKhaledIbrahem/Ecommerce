package com.example.ecommerce.features.address.data.datasources

import com.example.ecommerce.features.address.data.models.AddressRequestModel
import com.example.ecommerce.features.address.data.models.AddressResponseModel
import com.example.ecommerce.features.address.data.models.CheckUpdateAddressResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface AddressApi {
    @PUT("wp-json/custom/v1/customer-address")
    suspend fun updateAddress(
        @Body updateAddressParams: AddressRequestModel
    ): Response<AddressResponseModel>

    @GET("wp-json/custom/v1/customer-info")
    suspend fun getAddress(): Response<AddressResponseModel>

    @GET("wp-json/custom/v1/check-address-updated")
    suspend fun checkUpdateAddress(): Response<CheckUpdateAddressResponseModel>

}