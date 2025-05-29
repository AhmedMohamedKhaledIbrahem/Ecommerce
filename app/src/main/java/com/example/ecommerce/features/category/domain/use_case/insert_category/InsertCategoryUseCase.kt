package com.example.ecommerce.features.category.domain.use_case.insert_category

import com.example.ecommerce.features.category.domain.repository.CategoryRepository
import javax.inject.Inject

interface InsertCategoryUseCase {
    suspend operator fun invoke()
}

class InsertCategoryUseCaseImp @Inject constructor(
    private val repository: CategoryRepository
) : InsertCategoryUseCase {
    override suspend fun invoke() {
        repository.insertCategory()
    }

}