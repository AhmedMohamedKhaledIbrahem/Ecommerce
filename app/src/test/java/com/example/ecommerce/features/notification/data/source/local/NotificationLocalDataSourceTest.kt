package com.example.ecommerce.features.notification.data.source.local

import com.example.ecommerce.core.database.data.dao.orders.OrderTagDao
import com.example.ecommerce.core.manager.fcm.FcmDeviceToken
import com.example.ecommerce.features.cacheFailureMessage
import com.example.ecommerce.features.failureException
import com.example.ecommerce.features.notification.tOrderId
import com.example.ecommerce.features.notification.tStatus
import com.example.ecommerce.features.notification.tToken
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
class NotificationLocalDataSourceTest {
    @Mock
    private lateinit var dao: OrderTagDao

    @Mock
    private lateinit var fcmDeviceToken: FcmDeviceToken
    private lateinit var localDataSource: NotificationLocalDataSource

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        localDataSource = NotificationLocalDataSourceImp(dao, fcmDeviceToken)
    }

    @Test
    fun `updateOrderStatus should call dao updateOrderStatus method`() = runTest {
        `when`(dao.updateOrderStatus(orderId = tOrderId, status = tStatus)).thenReturn(Unit)
        localDataSource.updateOrderStatus(orderId = tOrderId, status = tStatus)
        verify(dao).updateOrderStatus(orderId = tOrderId, status = tStatus)
    }

    @Test
    fun `updateOrderStatus should be thrown when exception occurs`() = runTest {
        `when`(dao.updateOrderStatus(orderId = tOrderId, status = tStatus))
            .thenThrow(RuntimeException(cacheFailureMessage))

        val exception = failureException {
            localDataSource.updateOrderStatus(orderId = tOrderId, status = tStatus)
        }
        assertEquals(cacheFailureMessage, exception.message)
    }

    @Test
    fun `addFcmTokenDevice should be save token in Shared Preferences`() = runTest {
        `when`(fcmDeviceToken.addFcmTokenDevice(token = tToken)).thenReturn(Unit)
        localDataSource.addFcmTokenDevice(token = tToken)
        verify(fcmDeviceToken).addFcmTokenDevice(token = tToken)
    }

    @Test
    fun `addFcmTokenDevice should be thrown when exception occurs`() = runTest {
        `when`(fcmDeviceToken.addFcmTokenDevice(token = tToken))
            .thenThrow(RuntimeException(cacheFailureMessage))
        val exception = failureException {
            localDataSource.addFcmTokenDevice(token = tToken)
        }
        assertEquals(cacheFailureMessage, exception.message)
    }

}