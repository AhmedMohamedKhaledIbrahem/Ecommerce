package com.example.ecommerce.core.database.data.dao.orders

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.example.ecommerce.core.database.data.entities.orders.OrderTagEntity
import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems

@Dao
interface OrderTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderTag(orderTag: OrderTagEntity)

    @Query("Delete from order_tags")
    suspend fun clearOrderTags()

    @Transaction
    @Query("select * from order_tags")
    suspend fun getOrderTags(): List<OrderWithItems>

    @Query("Update order_tags set status = :status where orderId = :orderId")
    suspend fun updateOrderStatus(orderId: Int, status: String)


}