package com.example.ecommerce.core.database.data.dao.logout

import androidx.room.Dao
import androidx.room.Query

@Dao
interface LogoutDao {
    @Query("Delete from customerAddress")
    suspend fun deleteCustomerAddress()

    @Query("Delete from cart")
    suspend fun deleteCart()

    @Query("Delete from items")
    suspend fun deleteItemCart()

    @Query("delete from product_category")
    suspend fun deleteProductCategory()

    @Query("Delete from category")
    suspend fun deleteCategory()

    @Query("Delete from image")
    suspend fun deleteImage()

    @Query("Delete from orders")
    suspend fun deleteOrders()

    @Query("Delete from order_tags")
    suspend fun deleteOrderTage()

    @Query("Delete from product")
    suspend fun deleteProduct()

    @Query("Delete from ProductCategoryCrossRefEntity")
    suspend fun deleteProductCategoryCross()

    @Query("Delete from user")
    suspend fun deleteUser()

}