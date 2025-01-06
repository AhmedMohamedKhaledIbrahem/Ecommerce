package com.example.ecommerce.features.product.data.datasource.localdatasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ecommerce.core.database.data.dao.product.ProductDao
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails
import com.example.ecommerce.core.errors.Failures
import javax.inject.Inject

class ProductPagingSource @Inject constructor(
    private val productDao: ProductDao,
) : PagingSource<Int, ProductWithAllDetails>() {

    override fun getRefreshKey(state: PagingState<Int, ProductWithAllDetails>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductWithAllDetails> {
        return try {
            val pageIndex = params.key ?: 0
            val pageSize = params.loadSize
            val products = productDao.getProducts(pageSize, pageIndex)
            LoadResult.Page(
                data = products,
                prevKey = if (pageIndex == 0) null else pageIndex - 1,
                nextKey = if (products.isEmpty()) null else pageIndex + 1
            )
        } catch (e: Exception) {
            val error = LoadResult.Error<Int, ProductWithAllDetails>(e).throwable.message
            throw Failures.ServerFailure("$error")
        }
    }
}