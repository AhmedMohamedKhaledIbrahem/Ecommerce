package com.example.ecommerce.features.notification.data.source.remote

import com.example.ecommerce.features.failureException
import com.example.ecommerce.features.notification.tErrorResponseFCMTokenBody
import com.example.ecommerce.features.notification.tExceptionError
import com.example.ecommerce.features.notification.tMapToken
import com.example.ecommerce.features.notification.tResponseFCMTokenBody
import com.example.ecommerce.features.notification.tToken
import com.example.ecommerce.features.serverFailureMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class NotificationRemoteDataSourceTest {
    @Mock
    private lateinit var api: FcmApi
    private lateinit var remoteDataSource: NotificationRemoteDataSource


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        remoteDataSource = NotificationRemoteDataSourceImp(api)
    }

    @Test
    fun `saveToken should return Unit when call the remote data source success`() = runTest {
        val responseBody = Response.success(tResponseFCMTokenBody)
        `when`(api.saveToken(token = tMapToken)).thenReturn(responseBody)
        remoteDataSource.saveToken(token = tToken)
        verify(api).saveToken(token = tMapToken)
    }

    @Test
    fun `saveToken should throw Failure exception when call the remote data source have code 400 or higher`() =
        runTest {
            val responseBody = Response.error<ResponseBody>(
                400,
                tErrorResponseFCMTokenBody
            )
            `when`(api.saveToken(token = tMapToken)).thenReturn(responseBody)
            val exception = failureException {
                remoteDataSource.saveToken(token = tToken)
            }
            assertEquals(tExceptionError, exception.message)
        }

    @Test
    fun `saveToken Should throw Failure exception when Api Call throws exception`() = runTest {
        `when`(api.saveToken(token = tMapToken)).thenThrow(RuntimeException(serverFailureMessage))
        val exception = failureException {
            remoteDataSource.saveToken(token = tToken)
        }
        assertEquals(serverFailureMessage, exception.message)

    }
}