package com.example.ecommerce.features.authentication.data.datasource.remotedatasource

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.authentication.data.datasources.AuthenticationApi
import com.example.ecommerce.features.authentication.data.datasources.remotedatasource.AuthenticationRemoteDataSourceImp
import com.example.ecommerce.features.authentication.data.models.AuthenticationRequestModel
import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel
import com.example.ecommerce.features.authentication.data.models.ChangePasswordRequestModel
import com.example.ecommerce.features.authentication.data.models.EmailRequestModel
import com.example.ecommerce.features.authentication.data.models.MessageResponseModel
import com.example.ecommerce.features.authentication.data.models.SignUpRequestModel
import com.example.ecommerce.features.errorBody
import com.example.ecommerce.features.errorJsonBody
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.failureException
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class AuthenticationRemoteDataSourceImpTest {

    private val authenticationApi: AuthenticationApi = mockk<AuthenticationApi>()
    private lateinit var remoteDataSource: AuthenticationRemoteDataSourceImp

    @Before
    fun setUp() {
        remoteDataSource = AuthenticationRemoteDataSourceImp(api = authenticationApi)
    }


    private val loginResponse = fixture("login.json").run {
        Gson().fromJson(this, AuthenticationResponseModel::class.java)
    }
    private val messageResponse = fixture("message.json").run {
        Gson().fromJson(this, MessageResponseModel::class.java)
    }
    private val tLoginParams = mockk<AuthenticationRequestModel>()
    private val tEmailParams = mockk<EmailRequestModel>()
    private val tSignUpParams = mockk<SignUpRequestModel>()


    @Test
    fun `login should return the AuthenticationResponse when the call remote Data Source is successful `(): Unit =
        runTest {
            val response = Response.success(loginResponse)

            coEvery { authenticationApi.loginRequest(tLoginParams) } returns response
            val result = remoteDataSource.login(loginParams = tLoginParams)
            assertEquals(loginResponse, result)
            coVerify(exactly = 1) {
                authenticationApi.loginRequest(tLoginParams)
            }
        }

    @Test
    fun `login should throw serverFailure when the call remote data source is code 400 or higher`(): Unit =
        runTest {

            val response = Response.error<AuthenticationResponseModel>(
                400,
                errorBody
            )
            coEvery { authenticationApi.loginRequest(tLoginParams) } returns response
            val result = failureException {
                remoteDataSource.login(tLoginParams)
            }
            assertEquals(errorMessage, result.message)
        }

    @Test
    fun `signUp should return the MessageResponse when the call remote data source is successful`(): Unit =
        runTest {

            val response = Response.success(messageResponse)
            coEvery { authenticationApi.signUpRequest(tSignUpParams) } returns response
            val result = remoteDataSource.signUp(tSignUpParams)
            assertEquals(messageResponse, result)
        }

    @Test
    fun `signUp should throw serverFailure when the call remote data source is code 400 or higher`() =
        runTest {
            val response = Response.error<MessageResponseModel>(
                400,
                errorBody
            )
            coEvery { authenticationApi.signUpRequest(tSignUpParams) } returns response
            val result = failureException {
                remoteDataSource.signUp(tSignUpParams)
            }
            assertEquals(errorMessage, result.message)

        }

    @Test
    fun `resetPassword should return the MessageResponse when the call remote data source is successful`(): Unit =
        runTest {
            val response = Response.success(messageResponse)
            coEvery { authenticationApi.resetPasswordRequest(tEmailParams) } returns response
            val result = remoteDataSource.resetPassword(tEmailParams)
            assertEquals(messageResponse, result)
        }

    @Test
    fun `ResetPassword should throw serverFailure when the call remote data source is code 400 or higher`(): Unit =
        runTest {
            val response = Response.error<MessageResponseModel>(
                400,
                errorJsonBody
            )
            coEvery { authenticationApi.resetPasswordRequest(tEmailParams) } returns response
            val result = assertFailsWith<FailureException> {
                remoteDataSource.resetPassword(tEmailParams)
            }
            assertEquals(errorMessage, result.message)
        }

    @Test
    fun `changePassword should return the MessageResponse when the call remote data source is successful`() =
        runTest {
            val changePasswordRequest = mockk<ChangePasswordRequestModel>()
            val changePasswordResponse = mockk<MessageResponseModel>()
            val response = Response.success(changePasswordResponse)
            coEvery { authenticationApi.changePasswordRequest(changePasswordRequest) } returns response
            val result = remoteDataSource.changePassword(changePasswordRequest)
            assertEquals(changePasswordResponse, result)

        }

}