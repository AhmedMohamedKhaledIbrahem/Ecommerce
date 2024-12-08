package com.example.ecommerce.features.authentication.data.datasources.localdatasource

import com.example.ecommerce.core.data.dao.UserDao
import com.example.ecommerce.core.data.entities.UserEntity
import com.example.ecommerce.core.errors.FailureException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationLocalDataSourceImp @Inject constructor(
    private val dao: UserDao
) : AuthenticationLocalDataSource {
    override suspend fun insertUser(userEntity: UserEntity) {
        withContext(Dispatchers.IO) {
            try {
                dao.insertUser(userEntity)
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }

    override suspend fun updateVerificationStatusByEmail(
        email: String,
        verificationStatus: Boolean
    ) {
        withContext(Dispatchers.IO) {
            try {
                dao.updateVerificationStatusByEmail(email, verificationStatus)
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }

    override suspend fun checkUserEntityById(userId: Int): UserEntity? {
        return withContext(Dispatchers.IO) {
            try {
                dao.getUserById(userId = userId)
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }
}