package com.example.ecommerce.features.product.data.datasource.localdatasource

import androidx.paging.PagingData

import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails
import com.example.ecommerce.features.product.data.model.EcommerceResponseModel
import kotlinx.coroutines.flow.Flow

interface ProductLocalDataSource {
    fun getProductsPaged(): Flow<PagingData<ProductWithAllDetails>>
    suspend fun insertProducts(page: Int, pageSize: Int, product: EcommerceResponseModel)
    fun searchProduct(query: String): Flow<PagingData<ProductWithAllDetails>>

}