package com.example.ecommerce.features.productsearch.domain.entites

data class ProductSearchEntity(
    val asin: String? = null,
    val climatePledgeFriendly: Boolean? = null,
    val currency: String? = null,
    val delivery: String? = null,
    val hasVariations: Boolean? = null,
    val isAmazonChoice: Boolean? = null,
    val isBestSeller: Boolean? = null,
    val isPrime: Boolean? = null,
    val productAvailability: String? = null,
    val productMinimumOfferPrice: String? = null,
    val productNumOffers: Int? = null,
    val productNumRatings: Int? = null,
    val productOriginalPrice: String? = null,
    val productPhoto: String? = null,
    val productPrice: String? = null,
    val productStarRating: String? = null,
    val productTitle: String? = null,
    val productUrl: String? = null,
    val salesVolume: String? = null
)
