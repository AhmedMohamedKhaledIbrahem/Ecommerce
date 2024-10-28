package com.example.ecommerce.features.productsearch.data.repositories

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.productsearch.data.datasources.ProductSearchRemoteDataSource
import com.example.ecommerce.features.productsearch.data.mapper.ProductMapper
import com.example.ecommerce.features.productsearch.domain.entites.ProductSearchEntity
import com.example.ecommerce.features.productsearch.domain.repositories.ProductSearchRepository
import javax.inject.Inject

class ProductSearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProductSearchRemoteDataSource,
    private val internetConnectionChecker: InternetConnectionChecker,
) : ProductSearchRepository {
    override suspend fun getProductsByCategory(categoryId: String): List<ProductSearchEntity> {
        return try {
            if (internetConnectionChecker.hasConnection()) {
                val productsSearchByCategory =
                    remoteDataSource.getProductByCategory(categoryId = categoryId)
                ProductMapper.mapToEntityList(productsSearchByCategory)
            } else {
                    throw  Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (e:FailureException) {
                throw Failures.ServerFailure(e.message?:"Unknown server error")
        }
    }
}