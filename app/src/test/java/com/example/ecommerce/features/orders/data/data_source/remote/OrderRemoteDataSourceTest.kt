package com.example.ecommerce.features.orders.data.data_source.remote

import com.example.ecommerce.core.customer.CustomerManager
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.errorBody
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.errorResponseBody
import com.example.ecommerce.features.orders.data.data_source.OrderApi
import com.example.ecommerce.features.orders.data.models.OrderResponseModel
import com.example.ecommerce.features.orders.tCreateOrderRequestModel
import com.example.ecommerce.features.orders.tCreateOrderResponseModelJson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class OrderRemoteDataSourceTest {
    @Mock
    private lateinit var api: OrderApi

    @Mock
    private lateinit var customerManager: CustomerManager
    private lateinit var remoteDataSource: OrderRemoteDataSourceImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        remoteDataSource = OrderRemoteDataSourceImp(
            orderApi = api,
            customerManager = customerManager
        )
    }

    @Test
    fun `createOrder should return response when call the remote data source success`() = runTest {
        val response = Response.success(tCreateOrderResponseModelJson)
        `when`(api.createOrder(tCreateOrderRequestModel)).thenReturn(response)
        val result = remoteDataSource.createOrder(tCreateOrderRequestModel)
        assertEquals(tCreateOrderResponseModelJson, result)
    }

    @Test
    fun `createOrder should throw Failure exception when the body of response return Empty`() =
        runTest {
            val response = Response.success<OrderResponseModel>(null)
            `when`(api.createOrder(tCreateOrderRequestModel)).thenReturn(response)
            val exception = assertFailsWith<FailureException> {
                remoteDataSource.createOrder(tCreateOrderRequestModel)
            }
            assertEquals(errorResponseBody, exception.message)
        }

    @Test
    fun `createOrder should throw Failure exception when call the remote data source have code 400 or higher`() =
        runTest {
            val response = Response.error<OrderResponseModel>(
                400,
                errorBody
            )
            `when`(api.createOrder(tCreateOrderRequestModel)).thenReturn(response)
            val exception = assertFailsWith<FailureException> {
                remoteDataSource.createOrder(tCreateOrderRequestModel)
            }
            assertEquals(errorMessage, exception.message)
        }

    @Test
    fun `createOrder Should throw Failure exception when Api Call throws exception`() = runTest {
        `when`(api.createOrder(tCreateOrderRequestModel)).thenThrow(
            RuntimeException(errorMessage)
        )
        val exception = assertFailsWith<FailureException> {
            remoteDataSource.createOrder(tCreateOrderRequestModel)
        }
        assertEquals(errorMessage, exception.message)
    }
}