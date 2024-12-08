package com.example.ecommerce.core.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address")
data class AddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val userId: Int,
    val firstName: String? = null ?: "",
    val lastName: String? = null ?: "",
    val email: String? = null ?: "",
    val phone: String? = null ?: "",
    val address: String? = null ?: "",
    val country: String? = null ?: "",
    val city: String? = null ?: "",
    val state: String? = null ?: "",
    val zipCode: String? = null ?: "",
)
