package com.example.ecommerce.core.database.data.entities.category

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity(
   // @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @PrimaryKey   val categoryIdJson: Int,
    val categoryName: String,
)
