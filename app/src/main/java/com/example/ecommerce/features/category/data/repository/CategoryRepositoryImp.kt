package com.example.ecommerce.features.category.data.repository

import android.util.Log
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.database.data.entities.category.CategoryEntity
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.category.data.data_source.local.CategoryLocalDataSource
import com.example.ecommerce.features.category.data.data_source.remote.CategoryRemoteDataSource
import com.example.ecommerce.features.category.data.mapper.toDomain
import com.example.ecommerce.features.category.data.mapper.toDomainList
import com.example.ecommerce.features.category.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImp @Inject constructor(
    private val localDataSource: CategoryLocalDataSource,
    private val remoteDataSource: CategoryRemoteDataSource,
    private val connectionChecker: InternetConnectionChecker
) : CategoryRepository {
    override suspend fun insertCategory() {
        try {
            if (!connectionChecker.hasConnection()) {
                throw Failures.ConnectionFailure(resourceId = R.string.no_internet_connection)
            }
            val remoteResponse = remoteDataSource.fetchCategories()
            Log.d("TAG", "insertCategory: $remoteResponse")
            val categories = remoteResponse.toDomainList()

            try {
                for (category in categories) {
                    Log.d("TAG", "insertCategory: $category")
                    localDataSource.insertCategory(category)
                }
            } catch (failureException: FailureException) {
                throw Failures.CacheFailure(failureException.message ?: Unknown_Error)

            }

        } catch (failureException: FailureException) {
            throw Failures.ServerFailure(failureException.message ?: Unknown_Error)
        }
    }

    override suspend fun getCategory(): List<CategoryEntity> {
        return try {
            localDataSource.getCategory()
        } catch (failureException: FailureException) {
            throw Failures.CacheFailure(failureException.message ?: Unknown_Error)

        }
    }
}