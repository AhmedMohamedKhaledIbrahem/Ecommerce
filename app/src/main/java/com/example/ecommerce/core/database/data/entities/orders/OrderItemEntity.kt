package com.example.ecommerce.core.database.data.entities.orders

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = OrderTagEntity::class,
            parentColumns = ["orderId"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OrderItemEntity(
    @PrimaryKey val lineItemId: Int,
    val orderId: Int,
    val productId:Int,
    val itemName:String,
    val priceItem:String,
    val quantity:Int,
    val image:String,

):Parcelable
