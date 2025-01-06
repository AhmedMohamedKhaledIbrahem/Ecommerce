package com.example.ecommerce.features.product.data.datasource.remotedatasource

import com.example.ecommerce.features.product.data.model.EcommerceResponseModel

interface ProductRemoteDataSource {
    suspend fun getProducts(): EcommerceResponseModel
}