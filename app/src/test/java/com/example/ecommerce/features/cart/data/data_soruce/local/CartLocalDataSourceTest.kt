package com.example.ecommerce.features.cart.data.data_soruce.local

import com.example.ecommerce.core.database.data.dao.cart.CartDao
import com.example.ecommerce.core.database.data.dao.cart.ItemCartDao
import com.example.ecommerce.features.cacheFailureMessage
import com.example.ecommerce.features.cart.dummyCartEntity
import com.example.ecommerce.features.cart.dummyCartResponseModel
import com.example.ecommerce.features.cart.dummyCartWithItemEntity
import com.example.ecommerce.features.cart.dummyItemEntity
import com.example.ecommerce.features.cart.keyItem
import com.example.ecommerce.features.failureException
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
class CartLocalDataSourceTest {
    @Mock
    private lateinit var cartDao: CartDao

    @Mock
    private lateinit var itemCartDao: ItemCartDao

    private lateinit var localDataSource: CartLocalDataSourceImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        localDataSource = CartLocalDataSourceImp(
            cartDao = cartDao,
            itemCartDao = itemCartDao
        )
    }

    @Test
    fun `insertCartWithItems should insert cart and items into the database`() = runTest {
        `when`(cartDao.insertCart(cart = dummyCartEntity)).thenReturn(Unit)
        `when`(itemCartDao.insertItem(item = dummyItemEntity)).thenReturn(Unit)
        localDataSource.insertCartWithItems(cartResponseModel = dummyCartResponseModel)
        verify(cartDao).insertCart(cart = dummyCartEntity)
        verify(itemCartDao).insertItem(item = dummyItemEntity)

    }

    @Test
    fun `insertCartWithItems should throw a FailureException when insertCart throws an exception`() =
        runTest {
            `when`(cartDao.insertCart(cart = dummyCartEntity)).thenThrow(
                RuntimeException(
                    cacheFailureMessage
                )
            )

            val result = failureException {
                localDataSource.insertCartWithItems(cartResponseModel = dummyCartResponseModel)
            }
            assertEquals(cacheFailureMessage, result.message)
        }

    @Test
    fun `insertCartWithItems should throw a FailureException when insertCartWithItems throws an exception`() =
        runTest {
            `when`(itemCartDao.insertItem(item = dummyItemEntity)).thenThrow(
                RuntimeException(
                    cacheFailureMessage
                )
            )
            val result = failureException {
                localDataSource.insertCartWithItems(cartResponseModel = dummyCartResponseModel)
            }
            assertEquals(cacheFailureMessage, result.message)
        }

    @Test
    fun `getCart should return cart with items from the database`() = runTest {
        `when`(cartDao.getCartWithItems()).thenReturn(dummyCartWithItemEntity)
        val result = localDataSource.getCart()
        assertEquals(dummyCartWithItemEntity, result)
        verify(cartDao).getCartWithItems()
    }

    @Test
    fun `getCart should throw a FailureException when getCart throws an exception`() = runTest {
        `when`(cartDao.getCartWithItems()).thenThrow(
            RuntimeException(
                cacheFailureMessage
            )
        )
        val result = failureException {
            localDataSource.getCart()
        }
        assertEquals(cacheFailureMessage, result.message)
    }

    @Test
    fun `removeItem should remove item from the database`() = runTest {
        `when`(itemCartDao.removeItem(keyItem = keyItem)).thenReturn(Unit)
        localDataSource.removeItem(keyItem = keyItem)
        verify(itemCartDao).removeItem(keyItem = keyItem)
    }

    @Test
    fun `removeItem should throw a FailureException when removeItem throws an exception`() =
        runTest {
            `when`(itemCartDao.removeItem(keyItem = keyItem)).thenThrow(
                RuntimeException(
                    cacheFailureMessage
                )
            )
            val result = failureException {
                localDataSource.removeItem(keyItem = keyItem)
            }
            assertEquals(cacheFailureMessage, result.message)
        }
    @Test
    fun `clearCart should clear cart from the database`() = runTest {
        `when`(cartDao.deleteCart()).thenReturn(Unit)
        localDataSource.clearCart()
        verify(cartDao).deleteCart()
    }

    @Test
    fun `clearCart should throw a FailureException when deleteCart throws an exception`() =
        runTest {
            `when`(cartDao.deleteCart()).thenThrow(
                RuntimeException(
                    cacheFailureMessage
                )
            )
            val result = failureException {
                localDataSource.clearCart()
            }
            assertEquals(cacheFailureMessage, result.message)
        }
}