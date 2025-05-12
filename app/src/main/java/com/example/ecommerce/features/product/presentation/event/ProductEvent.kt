package com.example.ecommerce.features.product.presentation.event

import com.example.ecommerce.features.product.presentation.screen.product_details.ProductDetails


sealed class ProductEvent {

    data object FetchProductPaging : ProductEvent()
    data object FetchProductRemote : ProductEvent()
    data object Searched : ProductEvent()
    data class SearchQuery(val query: String) : ProductEvent()
    data object OnClickProductCard : ProductEvent()
    data class InputProductDetails(val product: ProductDetails) : ProductEvent()
}