package com.example.ecommerce.core.database.data.entities.orders

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "order_tags")
data class OrderTagEntity(
    @PrimaryKey val orderId:Int,
    val status:String,
    val paymentMethod:String,
    val paymentMethodTitle:String,
    val currency :String,
    val dateCreatedOrder:String,
    val totalPrice:String,
    val orderTagNumber :String
): Parcelable
