package com.example.ecommerce.features.orders.data.data_source.local

import android.util.Log
import com.example.ecommerce.core.database.data.dao.orders.OrderItemDao
import com.example.ecommerce.core.database.data.dao.orders.OrderTagDao
import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.orders.data.mapper.OrderItemMapper
import com.example.ecommerce.features.orders.data.mapper.OrderTagMapper
import com.example.ecommerce.features.orders.data.models.OrderResponseModel
import javax.inject.Inject

class OrderLocalDataSourceImp @Inject constructor(
    private val orderTagDao: OrderTagDao,
    private val orderItemDao: OrderItemDao
) : OrderLocalDataSource {
    override suspend fun insertOrderWithItem(orderResponseModel: OrderResponseModel) {
        try {
            val orderTagEntity =
                OrderTagMapper.mapToEntity(orderTagResponseModel = orderResponseModel)
            val orderItemEntity = orderResponseModel.lineItems.map {
                OrderItemMapper.mapToEntity(
                    lineItemResponseModel = it,
                    orderId = orderResponseModel.id
                )
            }
            orderTagDao.insertOrderTag(orderTag = orderTagEntity)
            orderItemDao.insertOrderItem(orderItemEntity = orderItemEntity)
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }


    override suspend fun getOrders(): List<OrderWithItems> {
        return try {
            orderTagDao.getOrderTags()
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }

    override suspend fun clearOrders() {
        try {
            orderTagDao.clearOrderTags()
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }
}