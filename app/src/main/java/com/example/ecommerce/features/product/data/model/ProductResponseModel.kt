package com.example.ecommerce.features.product.data.model

import com.google.gson.annotations.SerializedName

data class ProductResponseModel(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("short_description") val shortDescription: String,
    @SerializedName("prices") val price: PriceResponseModel,
    @SerializedName("review_count") val reviewCount: Int,
    @SerializedName("rating_count") val ratingCount: Float,
    @SerializedName("images") val image: List<ImageResponseModel>,
    @SerializedName("categories") val categories: List<CategoriesResponseModel>,
    @SerializedName("stock") val stock: StockResponseModel,
)