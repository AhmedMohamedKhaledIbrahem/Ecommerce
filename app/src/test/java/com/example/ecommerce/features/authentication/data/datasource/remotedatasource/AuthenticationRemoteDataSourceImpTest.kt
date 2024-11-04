package com.example.ecommerce.features.authentication.data.datasource.remotedatasource

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.authentication.data.datasources.AuthenticationApi
import com.example.ecommerce.features.authentication.data.datasources.remotedatasource.AuthenticationRemoteDataSourceImp
import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel
import com.example.ecommerce.features.authentication.data.models.MessageResponseModel
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class AuthenticationRemoteDataSourceImpTest {
    @Mock
    private lateinit var authenticationApi: AuthenticationApi
    private lateinit var remoteDataSource: AuthenticationRemoteDataSourceImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        remoteDataSource = AuthenticationRemoteDataSourceImp(api = authenticationApi)
    }

    private val tLoginParams = AuthenticationRequestEntity(userName = "test", password = "123456")
    private val loginResponse = fixture("login.json").run {
        Gson().fromJson(this, AuthenticationResponseModel::class.java)
    }
    private val tSignUpParams = SignUpRequestEntity(
        username = "test",
        email = "test@gmail.com" ,
        firstName = "jino",
        lastName = "pero",
        password = "123456",
    )
    private val tEmailRequestEntity = EmailRequestEntity(email = "test@gmail.com")
    private val messageResponse = fixture("message.json").run {
        Gson().fromJson(this,MessageResponseModel::class.java)
    }
    val objectJson: Pair<MediaType?, String> = Pair(
        MediaType.parse("application/json"),
        "{\"message\": \"server error\"}"
    )


    @Test
    fun `login should return the AuthenticationResponse when the call remote Data Source is successful `
        (): Unit =runTest {
            val response = Response.success(loginResponse)
            `when`(authenticationApi.loginRequest(tLoginParams)).thenReturn(response)
            val result = remoteDataSource.login(loginParams=tLoginParams)
            assertEquals(loginResponse,result)
            verify(authenticationApi).loginRequest(tLoginParams)

    }
    @Test
    fun `login should throw serverFailure when the call remote data source is code 400 or higher`
         ():Unit = runTest {

        val response = Response.error<AuthenticationResponseModel>(
            400,
            okhttp3.ResponseBody.create(null,"{'message': 'Some error message'}")
        )
        `when`(authenticationApi.loginRequest(tLoginParams)).thenReturn(response)

        val result = assertFailsWith<FailureException> {
            remoteDataSource.login(tLoginParams)
        }
        assertTrue(result.message!!.startsWith("Server Error:"))
    }

    @Test
    fun `signUp should return the MessageResponse when the call remote data source is successful`
         ():Unit = runTest {
             val response = Response.success(messageResponse)
             `when`(authenticationApi.signUpRequest(tSignUpParams)).thenReturn(response)
             val result = remoteDataSource.signUp(tSignUpParams)
             assertEquals(messageResponse , result)
    }
    @Test
    fun `signUp should throw serverFailure when the call remote data source is code 400 or higher`() = runTest {
        val response = Response.error<MessageResponseModel>(
            400,
            okhttp3.ResponseBody.create(null, "{'message': 'Some error message'}")
        )

        `when`(authenticationApi.signUpRequest(tSignUpParams)).thenReturn(response)

        val result = assertFailsWith<FailureException> {
            remoteDataSource.signUp(tSignUpParams)
        }

        assertTrue(result.message!!.startsWith("Server Error:"))
    }

    @Test
    fun `resetPassword should return the MessageResponse when the call remote data source is successful`
                ():Unit = runTest {
        val response = Response.success(messageResponse)
        `when`(authenticationApi.resetPasswordRequest(tEmailRequestEntity)).thenReturn(response)
        val result = remoteDataSource.resetPassword(tEmailRequestEntity)
        assertEquals(messageResponse , result)
    }
    @Test
    fun `ResetPassword should throw serverFailure when the call remote data source is code 400 or higher`
                ():Unit = runTest {
        val response = Response.error<MessageResponseModel>(
            400,
            okhttp3.ResponseBody.create(null , "{'message': 'Some error message'}")
        )
        `when`(authenticationApi.resetPasswordRequest(tEmailRequestEntity)).thenReturn(response)
        val result = assertFailsWith<FailureException> {
            remoteDataSource.resetPassword(tEmailRequestEntity)
        }
        assertTrue(result.message!!.startsWith("Server Error:"))
    }




}