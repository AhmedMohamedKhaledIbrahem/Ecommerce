package com.example.ecommerce.core.database.data.dao.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.core.database.data.entities.category.ProductCategoryEntity

@Dao
interface ProductCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<ProductCategoryEntity>)

    @Query("SELECT * FROM product_category")
    suspend fun getAllCategories(): List<ProductCategoryEntity>

    @Query("delete from product_category")
    suspend fun delete()


}