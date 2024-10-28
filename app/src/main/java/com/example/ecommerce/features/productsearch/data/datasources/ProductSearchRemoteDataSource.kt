package com.example.ecommerce.features.productsearch.data.datasources

import com.example.ecommerce.features.productsearch.data.models.ProductSearchModel

interface ProductSearchRemoteDataSource {
    suspend fun getProductByCategory(categoryId: String):List<ProductSearchModel>
}