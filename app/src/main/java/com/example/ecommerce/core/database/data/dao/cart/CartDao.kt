package com.example.ecommerce.core.database.data.dao.cart

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.ecommerce.core.database.data.entities.cart.CartEntity
import com.example.ecommerce.core.database.data.entities.cart.CartWithItems

interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: CartEntity)

    @Update
    suspend fun updateCart(cart: CartEntity)

    @Query("delete from cart")
    suspend fun clearCart()

    @Transaction
    @Query("select * from cart")
    suspend fun getCartWithItems(): CartWithItems


}