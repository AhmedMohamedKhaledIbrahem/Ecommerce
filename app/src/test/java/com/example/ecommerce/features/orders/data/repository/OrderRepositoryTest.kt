package com.example.ecommerce.features.orders.data.repository

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.checkInternet
import com.example.ecommerce.features.connectionFailure
import com.example.ecommerce.features.connectionFailureMessage
import com.example.ecommerce.features.orders.data.data_source.local.OrderLocalDataSource
import com.example.ecommerce.features.orders.data.data_source.remote.OrderRemoteDataSource
import com.example.ecommerce.features.orders.tCreateOrderRequestEntity
import com.example.ecommerce.features.orders.tCreateOrderRequestModel
import com.example.ecommerce.features.orders.tCreateOrderResponseModelJson
import com.example.ecommerce.features.serverFailure
import com.example.ecommerce.features.serverFailureMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class OrderRepositoryTest {
    @Mock
    private lateinit var remoteDataSource: OrderRemoteDataSource

    @Mock
    private lateinit var localDataSource: OrderLocalDataSource

    @Mock
    private lateinit var internetConnectionChecker: InternetConnectionChecker

    private lateinit var repository: OrderRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = OrderRepositoryImp(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            internetConnectionChecker = internetConnectionChecker
        )
    }

    @Test
    fun `createOrder should  check out order when the internet connection is available`() =
        runTest {
            checkInternet(true, internetConnectionChecker)
            `when`(remoteDataSource.createOrder(tCreateOrderRequestModel)).thenReturn(
                tCreateOrderResponseModelJson
            )
            val result = repository.createOrder(tCreateOrderRequestEntity)
//            assertEquals(tCreateOrderResponseEntity, result)
        }

    @Test
    fun `createOrder should throw exception ConnectionFailure when the internet connection isn't available`() =
        runTest {
            checkInternet(false, internetConnectionChecker)
            val result = connectionFailure {
                repository.createOrder(tCreateOrderRequestEntity)
            }
            assertEquals(connectionFailureMessage, result.message)
        }

    @Test
    fun `createOrder should throw exception ServerFailure when the internet connection is available`() =
        runTest {
            checkInternet(true, internetConnectionChecker)
            `when`(remoteDataSource.createOrder(tCreateOrderRequestModel)).thenThrow(
                FailureException(serverFailureMessage)
            )
            val result = serverFailure {
                repository.createOrder(tCreateOrderRequestEntity)
            }
            assertEquals(serverFailureMessage, result.message)
        }

}