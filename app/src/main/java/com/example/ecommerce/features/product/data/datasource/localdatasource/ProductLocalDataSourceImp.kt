package com.example.ecommerce.features.product.data.datasource.localdatasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.ecommerce.core.constants.EnablePlaceholders
import com.example.ecommerce.core.constants.IpHost
import com.example.ecommerce.core.constants.LocalHost
import com.example.ecommerce.core.constants.PageSize
import com.example.ecommerce.core.database.data.dao.category.ProductCategoryDao
import com.example.ecommerce.core.database.data.dao.image.ImageDao
import com.example.ecommerce.core.database.data.dao.product.ProductCategoryCrossRefDao
import com.example.ecommerce.core.database.data.dao.product.ProductDao
import com.example.ecommerce.core.database.data.entities.category.ProductCategoryEntity
import com.example.ecommerce.core.database.data.entities.image.ImageEntity
import com.example.ecommerce.core.database.data.entities.products.ProductCategoryCrossRefEntity
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.product.data.mapper.ProductMapper
import com.example.ecommerce.features.product.data.model.EcommerceResponseModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductLocalDataSourceImp @Inject constructor(
    private val productDao: ProductDao,
    private val imageDao: ImageDao,
    private val categoryDao: ProductCategoryDao,
    private val productCategoryDao: ProductCategoryCrossRefDao
) : ProductLocalDataSource {
    override fun getProductsPaged(): Flow<PagingData<ProductWithAllDetails>> {
        return try {
            Pager(
                config = PagingConfig(
                    pageSize = PageSize, enablePlaceholders = EnablePlaceholders,
                    initialLoadSize = PageSize
                ),
                pagingSourceFactory = { ProductPagingSource(productDao) }
            ).flow
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }

    override suspend fun isEmpty(): Boolean {
        return try {
            productDao.getProductsCount() == 0
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }

    override suspend fun insertProducts(
        page: Int,
        pageSize: Int,
        productResponse: EcommerceResponseModel
    ) = coroutineScope {
        try {
            val productEntity = productResponse.products.map {
                ProductMapper.toEntity(it)
            }
            val imageEntity = productResponse.products.flatMap { product ->
                product.image.map { image ->

                    ImageEntity(
                        productId = product.id,
                        imageId = image.id,
                        imageUrl = image.src.image.replace(LocalHost, IpHost)
                    )
                }
            }

            val productCategoryEntity = productResponse.products.flatMap { product ->
                product.categories.map { category ->
                    ProductCategoryEntity(
                        categoryIdJson = category.id,
                        categoryName = category.name
                    )
                }
            }
            val crossRefEntity = productResponse.products.flatMap { product ->
                product.categories.map { category ->
                    ProductCategoryCrossRefEntity(
                        productIdJson = product.id,
                        categoryIdJson = category.id
                    )
                }
            }
            productDao.insertProducts(productEntity)
            imageDao.insertImages(imageEntity)
            categoryDao.insertCategories(productCategoryEntity)
            productCategoryDao.insertProductCategoryCrossRef(crossRefEntity)

        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }

    }

    override fun searchProduct(query: String): Flow<PagingData<ProductWithAllDetails>> {
        return try {

            val pager = Pager(
                config = PagingConfig(
                    pageSize = PageSize, enablePlaceholders = EnablePlaceholders,
                    initialLoadSize = PageSize
                ),
                pagingSourceFactory = { ProductSearchPagingSource(productDao, query) }
            )
            pager.flow

        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }
}