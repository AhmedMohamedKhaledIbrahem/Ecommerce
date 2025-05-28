package com.example.ecommerce.core.database.data.entities.category

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey() val id: Int = 0,
    val name: String = "",
)
