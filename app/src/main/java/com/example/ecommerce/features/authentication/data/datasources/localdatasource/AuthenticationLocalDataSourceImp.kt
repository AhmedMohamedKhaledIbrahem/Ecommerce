package com.example.ecommerce.features.authentication.data.datasources.localdatasource

import com.example.ecommerce.core.database.data.dao.user.UserDao
import com.example.ecommerce.core.database.data.entities.user.UserEntity
import com.example.ecommerce.core.errors.FailureException
import javax.inject.Inject

class AuthenticationLocalDataSourceImp @Inject constructor(
    private val dao: UserDao
) : AuthenticationLocalDataSource {
    override suspend fun insertUser(userEntity: UserEntity) {
        try {
            dao.insertUser(userEntity)
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }

    }

    override suspend fun updateVerificationStatusByEmail(
        email: String,
        verificationStatus: Boolean
    ) {
        try {
            dao.updateVerificationStatusByEmail(email, verificationStatus)
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }

    }

    override suspend fun checkUserEntityById(userId: Int): UserEntity? {
        return try {
            dao.getUserById(userId = userId)
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }

    }
}