package com.example.ecommerce.features.orders.data.data_source.local

import com.example.ecommerce.core.database.data.entities.image.ImageEntity
import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems
import com.example.ecommerce.features.orders.data.models.OrderResponseModel

interface OrderLocalDataSource {
    suspend fun insertOrderWithItem(orderResponseModel: OrderResponseModel ,image: List<ImageEntity>)
    suspend fun getImagesByProductId(productsId: List<Int>): List<ImageEntity>
    suspend fun getOrders(): List<OrderWithItems>
    suspend fun clearOrders()

}