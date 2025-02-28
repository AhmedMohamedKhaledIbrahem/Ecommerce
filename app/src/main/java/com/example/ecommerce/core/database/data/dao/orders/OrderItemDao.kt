package com.example.ecommerce.core.database.data.dao.orders

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity

@Dao
interface OrderItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(orderItemEntity: List<OrderItemEntity>)

}