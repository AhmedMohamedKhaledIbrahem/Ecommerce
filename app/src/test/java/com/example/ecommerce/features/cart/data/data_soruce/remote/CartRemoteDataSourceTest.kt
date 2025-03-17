package com.example.ecommerce.features.cart.data.data_soruce.remote

import com.example.ecommerce.features.cart.data.data_soruce.CartApi
import com.example.ecommerce.features.cart.data.models.CartResponseModel
import com.example.ecommerce.features.cart.dummyAddItemRequestModel
import com.example.ecommerce.features.cart.dummyCartResponseModel
import com.example.ecommerce.features.cart.keyItem
import com.example.ecommerce.features.errorBody
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.errorResponseBody
import com.example.ecommerce.features.failureException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import retrofit2.Response
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class CartRemoteDataSourceTest {
    @Mock
    private lateinit var api: CartApi
    private lateinit var remoteDataSource: CartRemoteDataSourceImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        remoteDataSource = CartRemoteDataSourceImp(cartApi = api)
    }

    @Test
    fun `addItemCart should return response when call the remote data source success`() = runTest {
        val response = Response.success(dummyCartResponseModel)
        `when`(api.addItemToCart(request = dummyAddItemRequestModel)).thenReturn(response)
        val result = remoteDataSource.addItemCart(addItemRequestModel = dummyAddItemRequestModel)
        assertEquals(dummyCartResponseModel, result)
    }

    @Test
    fun `addItemCart should throw Failure exception when the body of response return Empty`() =
        runTest {
            val response = Response.success<CartResponseModel>(null)
            `when`(api.addItemToCart(request = dummyAddItemRequestModel)).thenReturn(response)
            val result = failureException {
                remoteDataSource.addItemCart(addItemRequestModel = dummyAddItemRequestModel)

            }
            assertEquals(errorResponseBody, result.message)
        }

    @Test
    fun `addItemCart should throw FailureException when call remote data source have code 400 or higher`() =
        runTest {
            val response = Response.error<CartResponseModel>(
                400,
                errorBody
            )
            `when`(api.addItemToCart(request = dummyAddItemRequestModel)).thenReturn(response)
            val result = failureException {
                remoteDataSource.addItemCart(addItemRequestModel = dummyAddItemRequestModel)

            }
            assertEquals(errorMessage, result.message)
        }

    @Test
    fun `addItemCart should throw FailureException when API call throws exception`() = runTest {
        `when`(api.addItemToCart(dummyAddItemRequestModel)).thenThrow(RuntimeException(errorMessage))
        val result = failureException {
            remoteDataSource.addItemCart(addItemRequestModel = dummyAddItemRequestModel)
        }
        assertEquals(errorMessage, result.message)
    }

    @Test
    fun `getCart should return response when call the remote data source success`() = runTest {
        val response = Response.success(dummyCartResponseModel)
        `when`(api.getCart()).thenReturn(response)
        val result = remoteDataSource.getCart()
        assertEquals(dummyCartResponseModel, result)
    }

    @Test
    fun `getCart should throw Failure exception when the body of response return Empty`() =
        runTest {
            val response = Response.success<CartResponseModel>(null)
            `when`(api.getCart()).thenReturn(response)
            val result = failureException {
                remoteDataSource.getCart()

            }
            assertEquals(errorResponseBody, result.message)
        }

    @Test
    fun `getCart should throw FailureException when call remote data source have code 400 or higher`() =
        runTest {
            val response = Response.error<CartResponseModel>(
                400,
                errorBody
            )
            `when`(api.getCart()).thenReturn(response)
            val result = failureException {
                remoteDataSource.getCart()

            }
            assertEquals(
                "A JSONObject text must begin with '{' at 0 [character 1 line 1]",
                result.message
            )
        }

    @Test
    fun `getCart should throw FailureException when API call throws exception`() = runTest {
        `when`(api.getCart()).thenThrow(RuntimeException(errorMessage))
        val result = failureException {
            remoteDataSource.getCart()
        }
        assertEquals(errorMessage, result.message)
    }

    @Test
    fun `deleteItemFromCart should return response when call the remote data source success`() =
        runTest {
            `when`(api.removeItem(keyItem = keyItem)).thenReturn(Unit)
            remoteDataSource.removeItem(itemHash = keyItem)
            verify(api).removeItem(keyItem = keyItem)
        }

    @Test
    fun `deleteItemFromCart  should throw FailureException when API call throws exception`() =
        runTest {
            `when`(api.removeItem(keyItem = keyItem)).thenThrow(
                RuntimeException(
                    errorMessage
                )
            )
            val result = failureException {
                remoteDataSource.removeItem(itemHash = keyItem)
            }
            assertEquals(errorMessage, result.message)
        }


    @Test
    fun `clearCart should return response when call the remote data source success`() =
        runTest {
            val response = Response.success<Unit>(null)
            `when`(api.clearCart()).thenReturn(response)
            remoteDataSource.clearCart()
            verify(api).clearCart()
        }

    @Test
    fun `clearCart should throw Failure exception when the status code is 400 or higher`() = runTest {
        val response = Response.error<Unit>(
            400,
            errorBody
        )
        `when`(api.clearCart()).thenReturn(response)
        val result = failureException {
            remoteDataSource.clearCart()
        }
        assertEquals(errorMessage, result.message)

    }

    @Test
    fun `clearCart  should throw FailureException when API call throws exception`() =
        runTest {

            `when`(api.clearCart()).thenThrow(
                RuntimeException(
                    errorMessage
                )
            )
            val result = failureException {
                remoteDataSource.clearCart()
            }
            assertEquals(errorMessage, result.message)
        }

}