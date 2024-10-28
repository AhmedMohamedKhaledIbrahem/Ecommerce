package com.example.ecommerce.features.productsearch.data.datasources

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.productsearch.data.models.ProductSearchModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductSearchRemoteDataSourceImpl @Inject constructor(
    private val productSearchApi: ProductSearchApi,
) : ProductSearchRemoteDataSource {
    private var page: Int = 1
    override suspend fun getProductByCategory(categoryId: String): List<ProductSearchModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    productSearchApi.getProductByCategory(categoryId = categoryId, page = page)
                if (response.isSuccessful) {
                    page++
                    response.body() ?: emptyList()

                } else {
                    throw FailureException("Server Error: ${response.message()}")
                }
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }

        }
    }
}