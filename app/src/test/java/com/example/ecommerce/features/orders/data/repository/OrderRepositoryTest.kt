package com.example.ecommerce.features.orders.data.repository

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.cacheFailure
import com.example.ecommerce.features.cacheFailureMessage
import com.example.ecommerce.features.checkInternet
import com.example.ecommerce.features.connectionFailure
import com.example.ecommerce.features.connectionFailureMessage
import com.example.ecommerce.features.orders.data.data_source.local.OrderLocalDataSource
import com.example.ecommerce.features.orders.data.data_source.remote.OrderRemoteDataSource
import com.example.ecommerce.features.orders.tCreateOrderRequestEntity
import com.example.ecommerce.features.orders.tCreateOrderRequestModel
import com.example.ecommerce.features.orders.tCreateOrderResponseModelJson
import com.example.ecommerce.features.orders.tImages
import com.example.ecommerce.features.orders.tImagesId
import com.example.ecommerce.features.orders.tOrderResponseEntity
import com.example.ecommerce.features.orders.tOrdersWithItems
import com.example.ecommerce.features.serverFailure
import com.example.ecommerce.features.serverFailureMessage
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
            assertEquals(tOrderResponseEntity, result)
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
    fun `createOrder should throw  ServerFailure when the internet connection is available`() =
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

    @Test
    fun `getOrders should  return the orders when the getOrders from local data source is available`() =
        runTest {
            `when`(localDataSource.getOrders()).thenReturn(
                tOrdersWithItems
            )
            val result = repository.getOrders()
            assertEquals(tOrdersWithItems, result)
        }


    @Test
    fun `getOrders should throw  CacheFailure when the getOrders from local data source is throw exception`() =
        runTest {

            `when`(localDataSource.getOrders()).thenThrow(
                FailureException(cacheFailureMessage)
            )
            val result = cacheFailure {
                repository.getOrders()
            }
            assertEquals(cacheFailureMessage, result.message)
        }

    @Test
    fun `saveOrderLocally should save orders when the saveOrderLocally from local data source is available`() =
        runTest {
            `when`(localDataSource.getImagesByProductId(tImagesId)).thenReturn(
                tImages
            )
            `when`(
                localDataSource.insertOrderWithItem(
                    tCreateOrderResponseModelJson,
                    tImages
                )
            ).thenReturn(
                Unit
            )
            repository.saveOrderLocally(tOrderResponseEntity)
            verify(localDataSource).getImagesByProductId(tImagesId)
            verify(localDataSource).insertOrderWithItem(tCreateOrderResponseModelJson, tImages)
        }


    @Test
    fun `saveOrderLocally should throw  CacheFailure when the getImagesByProductId from local data source is throw exception`() =
        runTest {
            `when`(localDataSource.getImagesByProductId(tImagesId)).thenThrow(
                FailureException(
                    cacheFailureMessage
                )
            )
            val result = cacheFailure {
                repository.saveOrderLocally(tOrderResponseEntity)
            }
            assertEquals(cacheFailureMessage, result.message)
        }

    @Test
    fun `saveOrderLocally should throw  CacheFailure when the insertOrderWithItem from local data source is throw exception`() =
        runTest {
            `when`(localDataSource.getImagesByProductId(tImagesId)).thenReturn(
                tImages
            )
            `when`(
                localDataSource.insertOrderWithItem(
                    tCreateOrderResponseModelJson,
                    tImages
                )
            ).thenThrow(
                FailureException(
                    cacheFailureMessage
                )
            )
            val result = cacheFailure {
                repository.saveOrderLocally(tOrderResponseEntity)
            }
            assertEquals(cacheFailureMessage, result.message)
        }

    @Test
    fun `clearOrders should clear orders when the clearOrders from local data source is available`() =
        runTest {

            `when`(
                localDataSource.clearOrders()
            ).thenReturn(
                Unit
            )
            repository.clearOrders()
            verify(localDataSource).clearOrders()

        }


    @Test
    fun `clearOrders should throw  CacheFailure when the clearOrders from local data source is throw exception`() =
        runTest {
            `when`(localDataSource.clearOrders()).thenThrow(
                FailureException(
                    cacheFailureMessage
                )
            )
            val result = cacheFailure {
                repository.clearOrders()
            }
            assertEquals(cacheFailureMessage, result.message)
        }


}