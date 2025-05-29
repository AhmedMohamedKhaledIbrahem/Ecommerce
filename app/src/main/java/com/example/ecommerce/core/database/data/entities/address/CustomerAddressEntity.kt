package com.example.ecommerce.core.database.data.entities.address

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "customerAddress")
data class CustomerAddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var firstName: String =  "",
    var lastName: String=  "",
    var email: String = "",
    var phone: String = "",
    var address: String=  "",
    var country: String =  "",
    var city: String =  "",
    var zipCode: String =  "",
    val isSelect: Int = 1
) : Parcelable
