package com.example.ecommerce.core.database.data.entities.image

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.ecommerce.core.database.data.entities.products.ProductEntity

@Entity(
    tableName = "image",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["productIdJson"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ImageEntity(
    //@PrimaryKey(autoGenerate = true) val id: Int = 0,
    @PrimaryKey  val imageId: Int,
    val productId: Int,
    val imageUrl: String
)

