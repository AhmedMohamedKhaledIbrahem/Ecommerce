package com.example.ecommerce.features.product.data.datasource.localdatasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerce.core.database.data.dao.category.CategoryDao
import com.example.ecommerce.core.database.data.dao.image.ImageDao
import com.example.ecommerce.core.database.data.dao.product.ProductCategoryCrossRefDao
import com.example.ecommerce.core.database.data.dao.product.ProductDao
import com.example.ecommerce.core.database.data.entities.category.CategoryEntity
import com.example.ecommerce.core.database.data.entities.image.ImageEntity
import com.example.ecommerce.core.database.data.entities.products.ProductCategoryCrossRefEntity
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.features.product.data.mapper.ProductMapper
import com.example.ecommerce.features.product.data.model.EcommerceResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductLocalDataSourceImp @Inject constructor(
    private val productDao: ProductDao,
    private val imageDao: ImageDao,
    private val categoryDao: CategoryDao,
    private val productCategoryDao: ProductCategoryCrossRefDao
) : ProductLocalDataSource {
    override fun getProductsPaged(): Flow<PagingData<ProductWithAllDetails>> {
        return try {
            Pager(
                config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                pagingSourceFactory = { ProductPagingSource(productDao) }
            ).flow
        } catch (e: Exception) {
            throw Failures.CacheFailure("${e.message}")
        }
    }

    override suspend fun insertProducts(
        page: Int,
        pageSize: Int,
        product: EcommerceResponseModel
    ) {
        withContext(Dispatchers.IO) {
            try {
                val productEntity = product.products.map {
                    ProductMapper.toEntity(it)
                }
                val imageEntity = product.products.flatMap { product ->
                    product.image.map { image ->

                        ImageEntity(
                            productId = product.id,
                            imageId = image.id,
                            imageUrl = image.src.image.replace("localhost", "10.0.0.106")
                        )
                    }
                }

                val categoryEntity = product.products.flatMap { product ->
                    product.categories.map { category ->
                        CategoryEntity(
                            categoryIdJson = category.id,
                            categoryName = category.name
                        )
                    }
                }
                val crossRefEntity = product.products.flatMap { product ->
                    product.categories.map { category ->
                        ProductCategoryCrossRefEntity(
                            productIdJson = product.id,
                            categoryIdJson = category.id
                        )
                    }
                }
                productDao.insertProducts(productEntity)
                imageDao.insertImages(imageEntity)
                categoryDao.insertCategories(categoryEntity)
                productCategoryDao.insertProductCategoryCrossRef(crossRefEntity)
            } catch (e: Exception) {
                throw Failures.CacheFailure("${e.message}")
            }
        }
    }

    override fun searchProduct(query: String): Flow<PagingData<ProductWithAllDetails>> {
        return try {

                val pager = Pager(
                    config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                    pagingSourceFactory = { ProductSearchPagingSource(productDao, query) }
                )
                pager.flow

        } catch (e: Exception) {
            throw Failures.CacheFailure("${e.message}")
        }
    }
}