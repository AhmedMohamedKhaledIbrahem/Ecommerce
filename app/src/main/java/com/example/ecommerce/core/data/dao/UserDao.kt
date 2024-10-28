package com.example.ecommerce.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.core.data.entities.UserEntity
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationResponseEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity:UserEntity)
    @Query("select * from user")
    suspend fun getUser():UserEntity

    @Query("delete from user")
    suspend fun delete()
}


/* @Query("UPDATE user SET firstName = :firstName, lastName = :lastName, displayName = :displayName WHERE userId = :userId")
    suspend fun updateUserProfile(userId: Int, firstName: String, lastName: String, displayName: String)*/