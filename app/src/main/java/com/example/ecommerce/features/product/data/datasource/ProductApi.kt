package com.example.ecommerce.features.product.data.datasource

import com.example.ecommerce.core.constants.GET_PRODUCTS_END_POINT
import com.example.ecommerce.features.product.data.model.EcommerceResponseModel
import com.example.ecommerce.features.product.data.model.ProductResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface ProductApi {
    @GET(GET_PRODUCTS_END_POINT)
    suspend fun getProducts(): Response<EcommerceResponseModel>
}