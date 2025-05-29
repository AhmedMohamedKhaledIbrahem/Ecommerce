package com.example.ecommerce.features.category.domain.use_case.get_category

import com.example.ecommerce.core.database.data.entities.category.CategoryEntity
import com.example.ecommerce.features.category.domain.repository.CategoryRepository
import jakarta.inject.Inject

interface GetCategoryUseCase {
    suspend operator fun invoke(): List<CategoryEntity>
}

class GetCategoryUseCaseImp @Inject constructor(
    private val repository: CategoryRepository
) : GetCategoryUseCase {
    override suspend fun invoke(): List<CategoryEntity> {
        return repository.getCategory()
    }

}