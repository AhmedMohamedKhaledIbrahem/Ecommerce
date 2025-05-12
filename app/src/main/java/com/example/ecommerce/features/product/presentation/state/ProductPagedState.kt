package com.example.ecommerce.features.product.presentation.state

import androidx.paging.PagingData
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails

data class ProductPagedState(
    val products: PagingData<ProductWithAllDetails> = PagingData.empty(),
    val isLoading: Boolean = false,
)

data class ProductRemoteState(
    val isLoading: Boolean = false,
)

