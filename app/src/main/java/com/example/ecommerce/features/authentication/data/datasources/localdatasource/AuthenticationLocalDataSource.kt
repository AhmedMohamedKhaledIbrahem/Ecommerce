package com.example.ecommerce.features.authentication.data.datasources.localdatasource

import com.example.ecommerce.core.database.data.entities.user.UserEntity

interface AuthenticationLocalDataSource {
    suspend fun insertUser(userEntity: UserEntity)
    suspend fun updateVerificationStatusByEmail(email:String,verificationStatus:Boolean)
    suspend fun checkUserEntityById(userId: Int): UserEntity?
   // suspend fun getUser():UserEntity
    //suspend fun delete()
}