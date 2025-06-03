package com.example.ecommerce.features.notification.data.source.remote

import com.example.ecommerce.features.errorBody
import com.example.ecommerce.features.errorJson
import com.example.ecommerce.features.errorJsonBody
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.failureException
import com.example.ecommerce.features.notification.data.model.NotificationRequestModel
import com.example.ecommerce.features.notification.data.model.NotificationResponseModel
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals


class NotificationRemoteDataSourceTest {
    private val api = mockk<FcmApi>()
    private lateinit var remoteDataSource: NotificationRemoteDataSource

    private val notificationRequestModel = mockk<NotificationRequestModel>()

    @Before
    fun setup() {
        remoteDataSource = NotificationRemoteDataSourceImp(api)
    }

    @Test
    fun `saveToken should call api saveToken with correct notificationRequestParams`() = runTest {
        val notificationResponseModel: NotificationResponseModel =
            fixture("notificationResponse.json").run {
                Gson().fromJson(this, NotificationResponseModel::class.java)
            }
        val response = Response.success(notificationResponseModel)
        coEvery { api.saveToken(notificationRequestModel) } returns response
        remoteDataSource.saveToken(notificationRequestModel)
        coVerify(exactly = 1) { api.saveToken(notificationRequestModel) }
    }

    @Test
    fun `saveToken should throw FailureException  when response is not successful`() =
        runTest {
            val errorCode = 400
            val response = Response.error<NotificationResponseModel>(errorCode, errorJsonBody)
            coEvery { api.saveToken(notificationRequestModel) } returns response
            val exception = failureException {
                remoteDataSource.saveToken(notificationRequestModel)
            }
            assertEquals(errorMessage, exception.message)

        }

    @Test
    fun `saveToken should throw FailureException on error`() = runTest {
        coEvery { api.saveToken(notificationRequestModel) } throws RuntimeException(RUN_TIME_ERROR)
        val exception = failureException {
            remoteDataSource.saveToken(notificationRequestModel)
        }
        assert(exception.message == RUN_TIME_ERROR)
    }

    companion object {
        private const val RUN_TIME_ERROR = "database error"
    }


}