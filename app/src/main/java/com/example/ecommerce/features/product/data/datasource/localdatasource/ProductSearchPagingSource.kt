package com.example.ecommerce.features.product.data.datasource.localdatasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ecommerce.core.database.data.dao.product.ProductDao
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails
import javax.inject.Inject

class ProductSearchPagingSource @Inject constructor(
    private val productDao: ProductDao,
    private val query: String // pass query if needed
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
            val products = productDao.searchProduct("%$query%", pageSize, pageIndex)
            LoadResult.Page(
                data = products.distinct(),
                prevKey = if (pageIndex == 0) null else pageIndex - 1,
                nextKey = if (products.isEmpty()) null else pageIndex + 1
            )
        } catch (e: Exception) {
            LoadResult.Error<Int, ProductWithAllDetails>(e)
        }
    }
}