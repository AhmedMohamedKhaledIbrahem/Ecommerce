package com.example.ecommerce.features.cart.data.repository

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.cacheFailure
import com.example.ecommerce.features.cacheFailureMessage
import com.example.ecommerce.features.cart.data.data_soruce.local.CartLocalDataSource
import com.example.ecommerce.features.cart.data.data_soruce.remote.CartRemoteDataSource
import com.example.ecommerce.features.cart.dummyAddItemRequestEntity
import com.example.ecommerce.features.cart.dummyAddItemRequestModel
import com.example.ecommerce.features.cart.dummyCartResponseModel
import com.example.ecommerce.features.cart.dummyCartWithItemEntity
import com.example.ecommerce.features.cart.keyItem
import com.example.ecommerce.features.checkInternet
import com.example.ecommerce.features.connectionFailure
import com.example.ecommerce.features.connectionFailureMessage
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
class CartRepositoryTest {
    @Mock
    private lateinit var remoteDataSource: CartRemoteDataSource

    @Mock
    private lateinit var localDataSource: CartLocalDataSource

    @Mock
    private lateinit var internetConnectionChecker: InternetConnectionChecker

    private lateinit var repository: CartRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = CartRepositoryImp(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            internetConnectionChecker = internetConnectionChecker
        )
    }


    @Test
    fun `addItem should insert cart with items when internet connection is available`() = runTest {
        checkInternet(true, internetConnectionChecker)
        `when`(remoteDataSource.addItemCart(addItemRequestModel = dummyAddItemRequestModel)).thenReturn(
            dummyCartResponseModel
        )
        `when`(localDataSource.insertCartWithItems(dummyCartResponseModel)).thenReturn(Unit)
        repository.addItem(addItemParams = dummyAddItemRequestEntity)
        verify(remoteDataSource).addItemCart(addItemRequestModel = dummyAddItemRequestModel)
        verify(localDataSource).insertCartWithItems(dummyCartResponseModel)
    }


    @Test
    fun `addItem should throw ConnectionFailure when internet connection is not available`() =
        runTest {
            checkInternet(false, internetConnectionChecker)
            val result = connectionFailure {
                repository.addItem(addItemParams = dummyAddItemRequestEntity)
            }
            assertEquals(connectionFailureMessage, result.message)

        }

    @Test
    fun `addItem should throw ServerFailure when internet connection  available`() = runTest {
        checkInternet(true, internetConnectionChecker)
        `when`(remoteDataSource.addItemCart(dummyAddItemRequestModel)).thenThrow(
            FailureException(serverFailureMessage)
        )
        val result = serverFailure {
            repository.addItem(addItemParams = dummyAddItemRequestEntity)
        }
        assertEquals(serverFailureMessage, result.message)

    }


    @Test
    fun `addItem should throw CacheFailure when internet connection  available`() = runTest {
        checkInternet(true, internetConnectionChecker)
        `when`(remoteDataSource.addItemCart(addItemRequestModel = dummyAddItemRequestModel)).thenReturn(
            dummyCartResponseModel
        )
        `when`(localDataSource.insertCartWithItems(dummyCartResponseModel)).thenThrow(
            FailureException(cacheFailureMessage)
        )
        val result = cacheFailure {
            repository.addItem(addItemParams = dummyAddItemRequestEntity)
        }
        assertEquals(cacheFailureMessage, result.message)

    }

    @Test
    fun `getCart should return cart with items when internet connection is available`() = runTest {
        checkInternet(true, internetConnectionChecker)
        `when`(localDataSource.getCart()).thenReturn(dummyCartWithItemEntity)
        val result = repository.getCart()
        assertEquals(dummyCartWithItemEntity, result)
        verify(localDataSource).getCart()
    }

    @Test
    fun `getCart should throw ConnectionFailure when the internet connection isn't available`() =
        runTest {
            checkInternet(false, internetConnectionChecker)
            val result = connectionFailure {
                repository.getCart()
            }
            assertEquals(connectionFailureMessage, result.message)
        }



    @Test
    fun `getCart should throw CacheFailure when the internet connection is available`() = runTest {
        checkInternet(true, internetConnectionChecker)
        `when`(remoteDataSource.getCart()).thenReturn(dummyCartResponseModel)
        `when`(localDataSource.getCart()).thenThrow(FailureException(cacheFailureMessage))
        val result = cacheFailure {
            repository.getCart()
        }
        assertEquals(cacheFailureMessage, result.message)

    }

    @Test
    fun `removeItem should return cart with items when internet connection is available`() =
        runTest {
            checkInternet(true, internetConnectionChecker)
            `when`(localDataSource.removeItem(keyItem = keyItem)).thenReturn(Unit)
            repository.removeItem(keyItem = keyItem)
            //verify(remoteDataSource).deleteItemFromCard(keyItem = keyItem)
            verify(localDataSource).removeItem(keyItem = keyItem)
        }

    @Test
    fun `removeItem should throw ConnectionFailure when the internet connection isn't available`() =
        runTest {
            checkInternet(false, internetConnectionChecker)
            val result = connectionFailure {
                repository.removeItem(keyItem = keyItem)
            }
            assertEquals(connectionFailureMessage, result.message)
        }



    @Test
    fun `removeItem should throw CacheFailure when the internet connection is available`() =
        runTest {
            checkInternet(true, internetConnectionChecker)
            `when`(remoteDataSource.deleteItemFromCard(keyItem = keyItem)).thenReturn(Unit)
            `when`(localDataSource.removeItem(keyItem = keyItem)).thenThrow(
                FailureException(
                    cacheFailureMessage
                )
            )
            val result = cacheFailure {
                repository.removeItem(keyItem = keyItem)
            }
            assertEquals(cacheFailureMessage, result.message)

        }


}