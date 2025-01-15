package com.example.ecommerce.core.database.data.dao.product

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.core.database.data.entities.products.ProductCategoryCrossRefEntity

@Dao
interface ProductCategoryCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductCategoryCrossRef(crossRef: List<ProductCategoryCrossRefEntity>)

    @Query("delete from ProductCategoryCrossRefEntity")
    suspend fun delete()

}