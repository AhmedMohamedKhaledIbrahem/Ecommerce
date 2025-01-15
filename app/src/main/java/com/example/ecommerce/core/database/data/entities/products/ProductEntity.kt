package com.example.ecommerce.core.database.data.entities.products

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "product",
)
data class ProductEntity(
    //@PrimaryKey(autoGenerate = true) val id: Int = 0,
    @PrimaryKey val productIdJson: Int,
    val name: String,
    val shortDescription: String,
    val description: String,
    val price: String,
    val reviewCount: Int,
    val ratingCount: Float,
    val isStock: Boolean,
    val statusStock: String,
    //val currency: String,

    )