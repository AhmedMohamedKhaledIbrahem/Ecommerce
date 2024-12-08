package com.example.ecommerce.features.userprofile.data.datasources.remotedatasource

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.userprofile.data.datasources.UserProfileApi
import com.example.ecommerce.features.userprofile.data.models.CheckUserNameDetailsResponseModel
import com.example.ecommerce.features.userprofile.data.models.GetImageProfileResponseModel
import com.example.ecommerce.features.userprofile.data.models.UpdateUserNameDetailsRequestModel
import com.example.ecommerce.features.userprofile.data.models.UpdateUserNameDetailsResponseModel
import com.example.ecommerce.features.userprofile.data.models.UploadImageProfileResponseModel
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import retrofit2.Response
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class UserProfileRemoteDataSourceImpTest {
    @Mock
    private lateinit var api: UserProfileApi
    private lateinit var remoteDataSource: UserProfileRemoteDataSourceImp

    private var dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        remoteDataSource = UserProfileRemoteDataSourceImp(api = api)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val tUpdateUserNameDetailsRequestModel = UpdateUserNameDetailsRequestModel(
        id = 1,
        firstName = "test",
        lastName = "test",
        displayName = "test test"
    )

    private val tUpdateUserNameDetailsResponseModel = fixture(
        "updateUserNameDetails.json"
    ).run {
        Gson().fromJson(this, UpdateUserNameDetailsResponseModel::class.java)
    }

    private val tCheckUserNameDetailsResponseModel = fixture(
        "checkUserNameDetailsUpdate.json"
    ).run {
        Gson().fromJson(this, CheckUserNameDetailsResponseModel::class.java)
    }

    private val tGetImageProfileResponseModel = fixture("getImageProfile.json").run {
        Gson().fromJson(this, GetImageProfileResponseModel::class.java)
    }
    private val tUploadImageProfileResponseModel = fixture("uploadImageProfile.json").run {
        Gson().fromJson(this, UploadImageProfileResponseModel::class.java)
    }
    private val tTempFile: File = File.createTempFile("test_image", ".jpg").apply {
        writeText("dummy content")
    }

    private val tUserId = 1
    private val errorBody = "{'message': 'error message'}".toResponseBody(null)
    private val errorResponseBody = "Empty Response Body"
    private val errorMessage = "error message"

    @Test
    fun `getImageProfileById should return response when call the remote data source success `() =
        runTest {
            val response = Response.success(tGetImageProfileResponseModel)
            `when`(api.getImageProfile(userId = tUserId)).thenReturn(response)
            val result = remoteDataSource.getImageProfileById(userId = tUserId)
            assertEquals(tGetImageProfileResponseModel, result)
            verify(api).getImageProfile(userId = tUserId)

        }

    @Test
    fun `getImageProfileById should throw Failure exception when the body of response return Empty`() =
        runTest {
            val response = Response.success<GetImageProfileResponseModel>(null)

            `when`(api.getImageProfile(userId = tUserId)).thenReturn(response)
            val exception = assertFailsWith<FailureException> {
                remoteDataSource.getImageProfileById(userId = tUserId)
            }
            assertEquals(errorResponseBody, exception.message)
        }

    @Test
    fun `getImageProfileById throws FailureException when call remote data source have code 400 or higher`() =
        runTest {
            val errorResponse = Response.error<GetImageProfileResponseModel>(
                400,
                errorBody
            )
            `when`(api.getImageProfile(userId = tUserId)).thenReturn(errorResponse)
            val exception = assertFailsWith<FailureException> {
                remoteDataSource.getImageProfileById(userId = tUserId)
            }
            assertEquals(errorMessage, exception.message)
        }

    @Test
    fun `uploadImageProfile should return response when call the remote data source success `() =
        runTest {
            val response = Response.success(tUploadImageProfileResponseModel)
            `when`(api.uploadImageProfile(any())).thenReturn(response)
            val result = remoteDataSource.uploadImageProfile(tTempFile)
            assertEquals(tUploadImageProfileResponseModel, result)
            tTempFile.delete()
        }

    @Test
    fun `uploadImageProfile should throw failure Exception when the body response return Empty`() =
        runTest {
            val response = Response.success<UploadImageProfileResponseModel>(null)
            `when`(api.uploadImageProfile(any())).thenReturn(response)
            val exception = assertFailsWith<FailureException> {
                remoteDataSource.uploadImageProfile(tTempFile)
            }
            assertEquals(errorResponseBody, exception.message)
        }

    @Test
    fun `uploadImageProfile should throw failureException when  call remote data source have code 400 or higher`() =
        runTest {
            val errorResponse = Response.error<UploadImageProfileResponseModel>(
                400,
                errorBody
            )
            `when`(api.uploadImageProfile(any())).thenReturn(errorResponse)
            val exception = assertFailsWith<FailureException> {
                remoteDataSource.uploadImageProfile(tTempFile)
            }
            assertEquals(errorMessage, exception.message)
        }

    @Test
    fun `getUserNameDetails should return response when call the remote data source success`() =
        runTest {
            val response = Response.success(tUpdateUserNameDetailsResponseModel)
            `when`(api.getUserNameDetails()).thenReturn(response)
            val result = remoteDataSource.getUserNameDetails()
            assertEquals(tUpdateUserNameDetailsResponseModel, result)
        }

    @Test
    fun `getUserNameDetails should throw failure Exception when the body response return Empty`() =
        runTest {
            val response = Response.success<UpdateUserNameDetailsResponseModel>(null)
            `when`(api.getUserNameDetails()).thenReturn(response)
            val exception = assertFailsWith<FailureException> {
                remoteDataSource.getUserNameDetails()
            }
            assertEquals(errorResponseBody, exception.message)
        }

    @Test
    fun `getUserNameDetails should throw failureException when  call remote data source have code 400 or higher`() =
        runTest {
            val response = Response.error<UpdateUserNameDetailsResponseModel>(
                400,
                errorBody
            )
            `when`(api.getUserNameDetails()).thenReturn(response)
            val exception = assertFailsWith<FailureException> {
                remoteDataSource.getUserNameDetails()
            }
            assertEquals(errorMessage, exception.message)
        }

    @Test
    fun `updateUserNameDetails should return response when call the remote data source success`() =
        runTest {
            val response = Response.success(tUpdateUserNameDetailsResponseModel)
            `when`(
                api.updateUserNameDetails(
                    updateUserNameDetailsParams = tUpdateUserNameDetailsRequestModel
                )
            ).thenReturn(response)
            val result = remoteDataSource.updateUserNameDetails(
                updateUserNameDetailsParams = tUpdateUserNameDetailsRequestModel
            )
            assertEquals(tUpdateUserNameDetailsResponseModel, result)
        }

    @Test
    fun `updateUserNameDetails should throw failure Exception when the body response return Empty`() =
        runTest {
            val response = Response.success<UpdateUserNameDetailsResponseModel>(null)
            `when`(
                api.updateUserNameDetails(
                    updateUserNameDetailsParams = tUpdateUserNameDetailsRequestModel
                )
            ).thenReturn(response)
            val exception = assertFailsWith<FailureException> {
                remoteDataSource.updateUserNameDetails(
                    updateUserNameDetailsParams = tUpdateUserNameDetailsRequestModel
                )
            }
            assertEquals(errorResponseBody, exception.message)
        }

    @Test
    fun `updateUserNameDetails should throw failureException when  call remote data source have code 400 or higher`() =
        runTest {
            val response = Response.error<UpdateUserNameDetailsResponseModel>(
                400,
                errorBody
            )
            `when`(
                api.updateUserNameDetails(
                    updateUserNameDetailsParams = tUpdateUserNameDetailsRequestModel
                )
            ).thenReturn(response)
            val exception = assertFailsWith<FailureException> {
                remoteDataSource.updateUserNameDetails(
                    updateUserNameDetailsParams = tUpdateUserNameDetailsRequestModel
                )
            }
            assertEquals(errorMessage, exception.message)
        }

    @Test
    fun `checkUserNameDetailsUpdate should return response when call the remote data source success`() =
        runTest {
            val response = Response.success(tCheckUserNameDetailsResponseModel)
            `when`(
                api.checkUserNameDetailsUpdate()
            ).thenReturn(response)
            val result = remoteDataSource.checkUserNameDetailsUpdate()
            assertEquals(tCheckUserNameDetailsResponseModel, result)
        }

    @Test
    fun `checkUserNameDetailsUpdate should throw failure Exception when the body response return Empty`() =
        runTest {
            val response = Response.success<CheckUserNameDetailsResponseModel>(null)
            `when`(
                api.checkUserNameDetailsUpdate()
            ).thenReturn(response)
            val exception = assertFailsWith<FailureException> {
                remoteDataSource.checkUserNameDetailsUpdate()
            }
            assertEquals(errorResponseBody, exception.message)
        }

    @Test
    fun `checkUserNameDetailsUpdate should throw failureException when  call remote data source have code 400 or higher`() =
        runTest {
            val response = Response.error<CheckUserNameDetailsResponseModel>(
                400,
                errorBody
            )
            `when`(
                api.checkUserNameDetailsUpdate()
            ).thenReturn(response)
            val exception = assertFailsWith<FailureException> {
                remoteDataSource.checkUserNameDetailsUpdate()
            }
            assertEquals(errorMessage, exception.message)
        }
}