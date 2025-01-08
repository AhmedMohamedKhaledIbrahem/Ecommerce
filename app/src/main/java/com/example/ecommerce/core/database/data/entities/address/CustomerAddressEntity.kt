package com.example.ecommerce.core.database.data.entities.address

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "customerAddress")
data class CustomerAddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @SerializedName("id") val userId: Int,
    @SerializedName("first_name") var firstName: String? = null ?: "",
    @SerializedName("last_name") var lastName: String? = null ?: "",
    @SerializedName("email") var email: String? = null ?: "",
    @SerializedName("phone") var phone: String? = null ?: "",
    @SerializedName("address_1") var address: String? = null ?: "",
    @SerializedName("country") var country: String? = null ?: "",
    @SerializedName("city") var city: String? = null ?: "",
    @SerializedName("state") var state: String? = null ?: "",
    @SerializedName("postcode") var zipCode: String? = null ?: "",
)
