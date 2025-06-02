package com.example.ecommerce.features.notification.data.repository

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.cacheFailure
import com.example.ecommerce.features.cacheFailureMessage
import com.example.ecommerce.features.notification.data.source.local.NotificationLocalDataSource
import com.example.ecommerce.features.notification.domain.repository.NotificationManagerRepository
import com.example.ecommerce.features.notification.tOrderId
import com.example.ecommerce.features.notification.tStatus
import com.example.ecommerce.features.notification.tToken
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class NotificationManagerRepositoryTest {
    @Mock
    private lateinit var localDataSource: NotificationLocalDataSource
    private lateinit var repository: NotificationManagerRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = NotificationManagerRepositoryImp(localDataSource = localDataSource)
    }

    @Test
    fun `addFcmTokenDevice should call localDataSource and added token in Shared Preference `() =
        runTest {
            `when`(localDataSource.addFcmTokenDevice(token = tToken)).thenReturn(Unit)
            repository.addFcmTokenDevice(token = tToken)
            verify(localDataSource).addFcmTokenDevice(token = tToken)

        }

    @Test
    fun `addFcmTokenDevice should throw CacheFailure when localDataSource throws exception`() =
        runTest {
            `when`(localDataSource.addFcmTokenDevice(token = tToken)).thenThrow(
                FailureException(
                    cacheFailureMessage
                )
            )
            val exception = cacheFailure {
                repository.addFcmTokenDevice(token = tToken)
            }
            assertEquals(cacheFailureMessage, exception.message)
        }


    @Test
    fun `updateOrderStatus should call localDataSource and update order status`() = runTest {
        `when`(localDataSource.updateOrderStatus(orderId = tOrderId, status = tStatus)).thenReturn(
            Unit
        )
        repository.updateOrderStatus(orderId = tOrderId, status = tStatus)
        verify(localDataSource).updateOrderStatus(orderId = tOrderId, status = tStatus)
    }

    @Test
    fun `updateOrderStatus should throw CacheFailure when localDataSource throws exception`() =
        runTest {
            `when`(
                localDataSource.updateOrderStatus(
                    orderId = tOrderId,
                    status = tStatus
                )
            ).thenThrow(
                FailureException(
                    cacheFailureMessage
                )
            )
            val exception = cacheFailure {
                repository.updateOrderStatus(orderId = tOrderId, status = tStatus)
            }
            assertEquals(cacheFailureMessage, exception.message)
        }
}