package com.example.ecommerce.features.category.presentation.state

import com.example.ecommerce.core.database.data.entities.category.CategoryEntity

data class CategoryState(
    val categories:List<CategoryEntity> = emptyList<CategoryEntity>(),
    val isFetched:Boolean = false,
)
