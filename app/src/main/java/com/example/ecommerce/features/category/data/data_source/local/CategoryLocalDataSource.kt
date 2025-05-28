package com.example.ecommerce.features.category.data.data_source.local

import com.example.ecommerce.core.database.data.dao.category.CategoryDao
import com.example.ecommerce.core.database.data.entities.category.CategoryEntity
import com.example.ecommerce.core.errors.FailureException
import javax.inject.Inject

interface CategoryLocalDataSource {
    suspend fun insertCategory(category: CategoryEntity)
    suspend fun getCategory(): List<CategoryEntity>
}

class CategoryLocalDataSourceImp @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryLocalDataSource {
    override suspend fun insertCategory(category: CategoryEntity) {
        try {
            categoryDao.insertCategories(categories = category)
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }

    override suspend fun getCategory(): List<CategoryEntity> {
        return try {
            categoryDao.getCategories()
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }

}