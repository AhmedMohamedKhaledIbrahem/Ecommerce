package com.example.ecommerce.features.userprofile.data.datasources.localdatasource

import com.example.ecommerce.core.database.data.dao.user.UserDao
import com.example.ecommerce.core.database.data.entities.user.UserEntity
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.userprofile.data.models.UpdateUserNameDetailsResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserProfileLocalDataSourceImp @Inject constructor(
    val dao: UserDao
) : UserProfileLocalDataSource {

    override suspend fun updateUserNameDetails(
        updateUserNameDetailsParams: UpdateUserNameDetailsResponseModel
    ) {
        withContext(Dispatchers.IO) {
            try {
                val user = dao.getUserById(updateUserNameDetailsParams.id)
                if (user != null) {
                    user.firstName = updateUserNameDetailsParams.firstName ?: ""
                    user.lastName = updateUserNameDetailsParams.lastName ?:""
                    user.displayName = updateUserNameDetailsParams.displayName ?:""
                    dao.updateUser(user)
                } else {
                    throw FailureException("User is not found")
                }
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }

    override suspend fun updateImageUserProfile(image: String ,userId:Int) {
        withContext(Dispatchers.IO){
            try {
                val user = dao.getUserById(userId)
                if (user != null){
                    user.imagePath = image
                    dao.updateUser(user)
                }else{
                    throw FailureException("User is not found")
                }
            }catch (e:Exception){
                throw FailureException("${e.message}")
            }
        }
    }


    override suspend fun getUserProfile(): UserEntity {
        return withContext(Dispatchers.IO) {
            try {
                dao.getUser()
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }
}