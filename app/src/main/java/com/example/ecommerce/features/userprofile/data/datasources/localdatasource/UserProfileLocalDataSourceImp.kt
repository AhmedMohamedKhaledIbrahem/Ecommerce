package com.example.ecommerce.features.userprofile.data.datasources.localdatasource

import com.example.ecommerce.core.data.dao.UserDao
import com.example.ecommerce.core.data.entities.UserEntity
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserNameDetailsRequestEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserProfileLocalDataSourceImp @Inject constructor(
    val dao: UserDao
) : UserProfileLocalDataSource {
    override suspend fun updateUserNameDetails(
        updateUserNameDetailsRequestEntity: UpdateUserNameDetailsRequestEntity
    ) {
        try {
            withContext(Dispatchers.IO) {
                val user = dao.getUserById(updateUserNameDetailsRequestEntity.id)
                if (user != null) {
                    user.firstName = updateUserNameDetailsRequestEntity.firstName
                    user.lastName = updateUserNameDetailsRequestEntity.lastName
                    user.displayName = updateUserNameDetailsRequestEntity.displayName
                    dao.updateUser(user)
                } else {
                    throw FailureException("User is not found")
                }
            }

        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }

    override suspend fun getUserProfile(): UserEntity {
        return try {
            withContext(Dispatchers.IO) {
                dao.getUser()
            }
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }
}