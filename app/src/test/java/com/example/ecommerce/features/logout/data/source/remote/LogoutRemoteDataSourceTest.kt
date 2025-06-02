package com.example.ecommerce.features.logout.data.source.remote

import com.example.ecommerce.features.failureException
import com.example.ecommerce.features.logout.data.source.LogoutApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals

class LogoutRemoteDataSourceTest {
    private val api = mockk<LogoutApi>()
    private lateinit var remoteDataSource: LogoutRemoteDataSource
    private val tToken = "ade:345"

    @Before
    fun setup() {
        remoteDataSource = LogoutRemoteDataSourceImp(api)

    }

    @Test
    fun `removeFcmToken should call api removeFcmToken with correct token`() = runTest {
        val response = Response.success(Unit)
        coEvery { api.removeFcmToken(token = tToken) } returns response
        remoteDataSource.removeFcmToken(tokenParams = tToken)
        coVerify(exactly = 1) { api.removeFcmToken(token = tToken) }
    }

    @Test
    fun `removeFcmToken should throw FailureException  when response is not successful`() =
        runTest {
            val errorCode = 400
            val responseBody = "".toResponseBody(null)
            val errorMessage = "status:$errorCode"
            val response = Response.error<Unit>(errorCode, responseBody)
            coEvery { api.removeFcmToken(token = tToken) } returns response
            val exception = failureException {
                remoteDataSource.removeFcmToken(tokenParams = tToken)
            }
            assertEquals(errorMessage, exception.message)

        }

    @Test
    fun `removeFcmToken should throw FailureException on error`() = runTest {
        coEvery { api.removeFcmToken(token = tToken) } throws RuntimeException(RUN_TIME_ERROR)
        val exception = failureException {
            remoteDataSource.removeFcmToken(tokenParams = tToken)
        }
        assert(exception.message == RUN_TIME_ERROR)
    }

    companion object {
        private const val RUN_TIME_ERROR = "database error"
    }
}


