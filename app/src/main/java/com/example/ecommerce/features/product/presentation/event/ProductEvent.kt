package com.example.ecommerce.features.product.presentation.event

import com.example.ecommerce.features.product.presentation.screen.product_details.ProductDetails


sealed class ProductEvent {

    data object FetchProductPaging : ProductEvent()
    data object FetchProductRemote : ProductEvent()
    data object Searched : ProductEvent()
    sealed class Input : ProductEvent() {
        data class SearchQuery(val query: String) : Input()
        data class InputProductDetails(val product: ProductDetails) : Input()
        data class FilterByCategory(val category: List<Int>) : Input()
    }
    data object OnFilterCategoryClick : ProductEvent()
    data object OnClickProductCard : ProductEvent()

}