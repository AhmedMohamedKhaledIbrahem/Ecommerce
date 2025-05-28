package com.example.ecommerce.features.category.data.model

import com.google.gson.annotations.SerializedName

data class CategoryResponseModel(
    @SerializedName("data")
    val data: List<DataCategoryResponseModel> = emptyList<DataCategoryResponseModel>()
)
