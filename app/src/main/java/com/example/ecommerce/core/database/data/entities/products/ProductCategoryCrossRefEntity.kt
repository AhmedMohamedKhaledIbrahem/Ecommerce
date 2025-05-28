package com.example.ecommerce.core.database.data.entities.products

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.ecommerce.core.database.data.entities.category.ProductCategoryEntity

@Entity(
    primaryKeys = ["productIdJson", "categoryIdJson"],
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["productIdJson"],
            childColumns = ["productIdJson"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductCategoryEntity::class,
            parentColumns = ["categoryIdJson"],
            childColumns = ["categoryIdJson"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProductCategoryCrossRefEntity(
    val productIdJson: Int,
    val categoryIdJson: Int,
)