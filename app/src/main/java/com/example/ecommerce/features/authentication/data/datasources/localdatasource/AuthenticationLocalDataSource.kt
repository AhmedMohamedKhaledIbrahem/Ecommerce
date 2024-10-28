package com.example.ecommerce.features.authentication.data.datasources.localdatasource

import com.example.ecommerce.core.data.entities.UserEntity

interface AuthenticationLocalDataSource {
    suspend fun insertUser(userEntity: UserEntity)
   // suspend fun getUser():UserEntity
    //suspend fun delete()
}