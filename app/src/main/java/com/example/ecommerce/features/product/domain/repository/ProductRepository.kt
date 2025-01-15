package com.example.ecommerce.features.product.domain.repository

import androidx.paging.PagingData
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun fetchProduct(page: Int, perPage: Int)
    fun getProducts(): Flow<PagingData<ProductWithAllDetails>>
    fun searchProduct(query: String): Flow<PagingData<ProductWithAllDetails>>

}