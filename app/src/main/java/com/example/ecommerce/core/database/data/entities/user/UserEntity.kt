package com.example.ecommerce.core.database.data.entities.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var userId: Int,
    val userEmail: String,
    val userName: String,
    val phone: String,
    var firstName: String,
    var lastName: String,
    val roles: String,
    val verificationStatues: Boolean,
    var displayName: String,
    var imagePath: String? = null ?: "",
    val expiredToken: Int

)
