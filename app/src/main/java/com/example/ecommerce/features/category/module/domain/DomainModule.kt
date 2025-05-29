package com.example.ecommerce.features.category.module.domain

import com.example.ecommerce.features.category.domain.repository.CategoryRepository
import com.example.ecommerce.features.category.domain.use_case.get_category.GetCategoryUseCase
import com.example.ecommerce.features.category.domain.use_case.get_category.GetCategoryUseCaseImp
import com.example.ecommerce.features.category.domain.use_case.insert_category.InsertCategoryUseCase
import com.example.ecommerce.features.category.domain.use_case.insert_category.InsertCategoryUseCaseImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    @Singleton
    fun provideGetCategoryUseCase(categoryRepository: CategoryRepository): GetCategoryUseCase {
        return GetCategoryUseCaseImp(categoryRepository)
    }

    @Provides
    @Singleton
    fun provideInsertCategoryUseCase(categoryRepository: CategoryRepository): InsertCategoryUseCase {
        return InsertCategoryUseCaseImp(categoryRepository)
    }
}