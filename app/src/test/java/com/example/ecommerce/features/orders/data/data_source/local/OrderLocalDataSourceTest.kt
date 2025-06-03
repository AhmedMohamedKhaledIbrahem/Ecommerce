package com.example.ecommerce.features.orders.data.data_source.local

import com.example.ecommerce.core.database.data.dao.image.ImageDao
import com.example.ecommerce.core.database.data.dao.orders.OrderItemDao
import com.example.ecommerce.core.database.data.dao.orders.OrderTagDao
import com.example.ecommerce.core.database.data.entities.image.ImageEntity
import com.example.ecommerce.features.cacheFailureMessage
import com.example.ecommerce.features.failureException
import com.example.ecommerce.features.orders.imageMessageError
import com.example.ecommerce.features.orders.tCreateOrderResponseModelJson
import com.example.ecommerce.features.orders.tImages
import com.example.ecommerce.features.orders.tOrderItemEntity
import com.example.ecommerce.features.orders.tOrderTagEntity
import com.example.ecommerce.features.orders.tOrderWithItems
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class OrderLocalDataSourceTest {
    @Mock
    private lateinit var orderTagDao: OrderTagDao

    @Mock
    private lateinit var orderItemDao: OrderItemDao

    @Mock
    private lateinit var imageDao: ImageDao
    private lateinit var localDataSource: OrderLocalDataSource
    private val orderWithItems = listOf(tOrderWithItems)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        localDataSource = OrderLocalDataSourceImp(
            orderTagDao = orderTagDao,
            orderItemDao = orderItemDao,
            imageDao = imageDao,
        )

    }

    @Test
    fun `insertOrderWithItem should insert order and items into the database`() = runTest {
        `when`(orderTagDao.insertOrderTag(orderTag = tOrderTagEntity)).thenReturn(Unit)
        `when`(orderItemDao.insertOrderItem(orderItemEntity = tOrderItemEntity)).thenReturn(Unit)
        localDataSource.insertOrderWithItem(
            orderResponseModel = tCreateOrderResponseModelJson,
            tImages
        )
        verify(orderTagDao).insertOrderTag(orderTag = tOrderTagEntity)
        verify(orderItemDao).insertOrderItem(orderItemEntity = tOrderItemEntity)
    }

    @Test
    fun `insertOrderWithItem should throw when the OrderTagDao throw Exception`() = runTest {
        `when`(orderTagDao.insertOrderTag(orderTag = tOrderTagEntity)).thenThrow(
            RuntimeException(cacheFailureMessage)
        )
        val exception = failureException {
            localDataSource.insertOrderWithItem(
                orderResponseModel = tCreateOrderResponseModelJson,
                tImages
            )
        }
        assertEquals(cacheFailureMessage, exception.message)
    }



    @Test
    fun `insertOrderWithItem should throw when the orderItemDao throw Exception`() = runTest {
        `when`(orderItemDao.insertOrderItem(orderItemEntity = tOrderItemEntity)).thenThrow(
            RuntimeException(cacheFailureMessage)
        )
        val exception = failureException {
            localDataSource.insertOrderWithItem(
                orderResponseModel = tCreateOrderResponseModelJson,
                tImages
            )
        }
        assertEquals(cacheFailureMessage, exception.message)
    }

    @Test
    fun `getOrders should return order with items from the database`() = runTest {
        `when`(orderTagDao.getOrderTags()).thenReturn(orderWithItems)
        val result = localDataSource.getOrders()
        assertEquals(orderWithItems, result)
        verify(orderTagDao).getOrderTags()
    }

    @Test
    fun `getOrders should throw when the OrderTagDao throw Exception`() = runTest {
        `when`(orderTagDao.getOrderTags()).thenThrow(
            RuntimeException(cacheFailureMessage)
        )
        val exception = failureException {
            localDataSource.getOrders()
        }
        assertEquals(cacheFailureMessage, exception.message)

    }

    @Test
    fun `clearOrders should remove order with items from the database`() = runTest {
        `when`(orderTagDao.clearOrderTags()).thenReturn(Unit)
        localDataSource.clearOrders()
        verify(orderTagDao).clearOrderTags()
    }

    @Test
    fun `clearOrders should throw when the OrderTagDao throw Exception`() = runTest {
        `when`(orderTagDao.clearOrderTags()).thenThrow(
            RuntimeException(cacheFailureMessage)
        )
        val exception = failureException {
            localDataSource.clearOrders()
        }
        assertEquals(cacheFailureMessage, exception.message)

    }
}