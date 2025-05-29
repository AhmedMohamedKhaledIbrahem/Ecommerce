package com.example.ecommerce.core.database.data.dao.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.core.database.data.entities.category.CategoryEntity
@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: CategoryEntity)

    @Query("SELECT * FROM category")
    suspend fun getCategories(): List<CategoryEntity>
    @Query("DELETE FROM category")
    suspend fun deleteCategory()

}