package com.example.ecommerce.features.product.presentation.state

import androidx.paging.PagingData
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails

data class ProductSearchState(
    val products: PagingData<ProductWithAllDetails> = PagingData.empty(),
    val query: String = "",
    val isSearching: Boolean = false,
)
