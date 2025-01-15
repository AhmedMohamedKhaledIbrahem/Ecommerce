package com.example.ecommerce.core.database.data.dao.cart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity

@Dao
interface ItemCartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: List<ItemCartEntity>)

    @Update
    suspend fun updateItem(item: List<ItemCartEntity>)

    @Query("delete from items where itemHashKey = :keyItem")
    suspend fun removeItem(keyItem: String)
}