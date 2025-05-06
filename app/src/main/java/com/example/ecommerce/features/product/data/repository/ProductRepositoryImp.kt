package com.example.ecommerce.features.product.data.repository

import androidx.paging.PagingData
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.manager.prdouct.ProductHandler
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.product.data.datasource.localdatasource.ProductLocalDataSource
import com.example.ecommerce.features.product.data.datasource.remotedatasource.ProductRemoteDataSource
import com.example.ecommerce.features.product.domain.repository.ProductRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImp @Inject constructor(
    private val productRemoteDataSource: ProductRemoteDataSource,
    private val productLocalDataSource: ProductLocalDataSource,
    private val productHandler: ProductHandler,
    private val checkInternetConnection: InternetConnectionChecker
) : ProductRepository {
    override suspend fun fetchProduct(page: Int, perPage: Int) = coroutineScope {
        try {
            if (checkInternetConnection.hasConnection()) {
                if (productLocalDataSource.isEmpty() || productHandler.isProductUpdate()) {

                    val remote = async {  productRemoteDataSource.getProducts()}
                    productLocalDataSource.insertProducts(page, perPage, remote.await())
                    productHandler.deleteProductUpdate()
                } else {
                    return@coroutineScope
                }
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (e: FailureException) {
            throw Failures.ServerFailure("${e.message}")
        }
    }

    override fun getProducts(): Flow<PagingData<ProductWithAllDetails>> {
        return try {
            productLocalDataSource.getProductsPaged()
        } catch (e: FailureException) {
            throw Failures.CacheFailure("${e.message}")
        }
    }

    override fun searchProduct(query: String): Flow<PagingData<ProductWithAllDetails>> {
        return try {
            productLocalDataSource.searchProduct(query = query)
        } catch (e: FailureException) {
            throw Failures.CacheFailure("${e.message}")
        }
    }
}