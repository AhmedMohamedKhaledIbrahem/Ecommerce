package com.example.ecommerce.features.product.presentation.state

import com.example.ecommerce.features.product.presentation.screen.product_details.ProductDetails

data class ProductDetailsState(
    val product: ProductDetails = ProductDetails(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0.0,
    ),
    val isLoading: Boolean = false,
)
