package com.example.ecommerce.core.database.data.entities.address

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customerAddress")
data class CustomerAddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val userId: Int,
    var firstName: String? = null ?: "",
    var lastName: String? = null ?: "",
    var email: String? = null ?: "",
    var phone: String? = null ?: "",
    var address: String? = null ?: "",
    var country: String? = null ?: "",
    var city: String? = null ?: "",
    var state: String? = null ?: "",
    var zipCode: String? = null ?: "",
)
