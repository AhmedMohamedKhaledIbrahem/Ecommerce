package com.example.ecommerce.core.database.data.entities.cart

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = CartEntity::class,
            parentColumns = ["cartId"],
            childColumns = ["cartId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index("cartId")]
)
data class ItemCartEntity(
    @PrimaryKey val itemId: Int,
    val itemHashKey: String,
    val cartId: String,
    val name: String,
    val image:String,
    val price: String,
    var quantity: Int,
)
