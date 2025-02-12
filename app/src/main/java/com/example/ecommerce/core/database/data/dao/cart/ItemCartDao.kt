package com.example.ecommerce.core.database.data.dao.cart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity

@Dao
interface ItemCartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: List<ItemCartEntity>)

    @Query("UPDATE items SET quantity = :newQuantity WHERE itemId = :itemId")
    suspend fun updateQuantity(itemId: Int, newQuantity: Int)

    @Query("delete from items where itemHashKey = :keyItem")
    suspend fun removeItem(keyItem: String)
}