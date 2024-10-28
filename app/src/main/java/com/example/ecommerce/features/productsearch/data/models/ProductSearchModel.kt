package com.example.ecommerce.features.productsearch.data.models


import com.google.gson.annotations.SerializedName

data class ProductSearchModel(
    @SerializedName("asin")
    val asin: String? = null,
    @SerializedName("climate_pledge_friendly")
    val climatePledgeFriendly: Boolean? = null,
    @SerializedName("currency")
    val currency: String? = null,
    @SerializedName("delivery")
    val delivery: String? = null,
    @SerializedName("has_variations")
    val hasVariations: Boolean? = null,
    @SerializedName("is_amazon_choice")
    val isAmazonChoice: Boolean? = null,
    @SerializedName("is_best_seller")
    val isBestSeller: Boolean? = null,
    @SerializedName("is_prime")
    val isPrime: Boolean? = null,
    @SerializedName("product_availability")
    val productAvailability: String? = null,
    @SerializedName("product_minimum_offer_price")
    val productMinimumOfferPrice: String? = null,
    @SerializedName("product_num_offers")
    val productNumOffers: Int? = null,
    @SerializedName("product_num_ratings")
    val productNumRatings: Int? = null,
    @SerializedName("product_original_price")
    val productOriginalPrice: String? = null,
    @SerializedName("product_photo")
    val productPhoto: String? = null,
    @SerializedName("product_price")
    val productPrice: String? = null,
    @SerializedName("product_star_rating")
    val productStarRating: String? = null,
    @SerializedName("product_title")
    val productTitle: String? = null,
    @SerializedName("product_url")
    val productUrl: String? = null,
    @SerializedName("sales_volume")
    val salesVolume: String? = null
)