package com.example.ecommerce.features.orders.data.data_source.local

import com.example.ecommerce.core.database.data.dao.image.ImageDao
import com.example.ecommerce.core.database.data.dao.orders.OrderItemDao
import com.example.ecommerce.core.database.data.dao.orders.OrderTagDao
import com.example.ecommerce.core.database.data.entities.image.ImageEntity
import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.orders.data.mapper.OrderItemMapper
import com.example.ecommerce.features.orders.data.mapper.OrderTagMapper
import com.example.ecommerce.features.orders.data.models.OrderResponseModel
import javax.inject.Inject

class OrderLocalDataSourceImp @Inject constructor(
    private val orderTagDao: OrderTagDao,
    private val orderItemDao: OrderItemDao,
    private val imageDao: ImageDao
) : OrderLocalDataSource {
    override suspend fun insertOrderWithItem(
        orderResponseModel: OrderResponseModel,
        image: List<ImageEntity>
    ) {
        try {
            val orderTagEntity =
                OrderTagMapper.mapToEntity(orderTagResponseModel = orderResponseModel)
            val orderItemEntity = orderResponseModel.lineItems.map { lineItem ->
                val imageUrl = image.find { it.productId == lineItem.productId }?.imageUrl
                //if (imageUrl == null) throw FailureException("image is Null")
                OrderItemMapper.mapToEntity(
                    lineItemResponseModel = lineItem,
                    orderId = orderResponseModel.id,
                    imageUrl = imageUrl?:""
                )
            }
            orderTagDao.insertOrderTag(orderTag = orderTagEntity)
            orderItemDao.insertOrderItem(orderItemEntity = orderItemEntity)
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }

    override suspend fun getImagesByProductId(productsId: List<Int>): List<ImageEntity> {
        return try {
            imageDao.getImagesByProductId(productsId = productsId)
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