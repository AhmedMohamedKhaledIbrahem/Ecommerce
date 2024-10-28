package com.example.ecommerce.core.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id :Int,
    val userId: Int,
    val userEmail: String,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val displayName: String,
    val expiredToken: Int
)
