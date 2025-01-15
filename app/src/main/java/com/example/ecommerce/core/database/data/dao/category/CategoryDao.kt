package com.example.ecommerce.core.database.data.dao.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.core.database.data.entities.category.CategoryEntity

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Query("SELECT * FROM category")
    suspend fun getAllCategories(): List<CategoryEntity>

    @Query("delete from category")
    suspend fun delete()


}