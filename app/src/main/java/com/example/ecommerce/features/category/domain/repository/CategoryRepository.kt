package com.example.ecommerce.features.category.domain.repository

import com.example.ecommerce.core.database.data.entities.category.CategoryEntity

interface CategoryRepository {
    suspend fun insertCategory()
    suspend fun getCategory(): List<CategoryEntity>
}