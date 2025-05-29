package com.example.ecommerce.core.database.data.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ecommerce.core.database.data.entities.user.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("select * from user")
    suspend fun getUser(): UserEntity

    @Query("select * from user where userId =:userId Limit 1")
    suspend fun getUserById(userId: Int): UserEntity?

    @Update
    suspend fun updateUser(userEntity: UserEntity)

    @Query("delete from user")
    suspend fun delete()

    @Query("Update user SET verificationStatues = :verificationStatus where userEmail =:email")
    suspend fun updateVerificationStatusByEmail(email: String, verificationStatus: Boolean)

    @Query("select Count(*) from user where userId =:userId and displayName =:displayName")
    suspend fun getUserExist(userId: Int, displayName: String): Int


}




