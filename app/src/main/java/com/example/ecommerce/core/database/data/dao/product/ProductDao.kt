package com.example.ecommerce.core.database.data.dao.product

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.ecommerce.core.database.data.entities.products.ProductEntity
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Transaction
    @Query("SELECT * FROM product order by productIdJson asc limit :pageSize offset :pageIndex * :pageSize")
    suspend fun getProducts(pageSize: Int, pageIndex: Int): List<ProductWithAllDetails>

    @Transaction
    @Query("select * from product where name like :query LIMIT :pageSize OFFSET :offset *:pageSize")
    fun searchProduct(query: String, pageSize: Int, offset: Int): List<ProductWithAllDetails>

    @Query("select count(*) from product")
    suspend fun getProductsCount(): Int


}