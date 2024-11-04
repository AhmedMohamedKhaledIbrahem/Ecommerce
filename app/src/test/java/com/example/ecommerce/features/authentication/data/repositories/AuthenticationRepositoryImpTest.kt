package com.example.ecommerce.features.authentication.data.repositories

import com.example.ecommerce.core.data.mapper.UserMapper
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.authentication.data.datasources.localdatasource.AuthenticationLocalDataSource
import com.example.ecommerce.features.authentication.data.datasources.localdatasource.AuthenticationSharedPreferencesDataSource
import com.example.ecommerce.features.authentication.data.datasources.remotedatasource.AuthenticationRemoteDataSource
import com.example.ecommerce.features.authentication.data.mapper.AuthenticationMapper
import com.example.ecommerce.features.authentication.data.mapper.MessageResponseMapper
import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel
import com.example.ecommerce.features.authentication.data.models.MessageResponseModel
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthenticationRepositoryImpTest {
    @Mock
    private lateinit var localDataSource: AuthenticationLocalDataSource

    @Mock
    private lateinit var remoteDataSource: AuthenticationRemoteDataSource

    @Mock
    private lateinit var sharedPreferences: AuthenticationSharedPreferencesDataSource

    @Mock
    private lateinit var networkInfo: InternetConnectionChecker
    private lateinit var repository: AuthenticationRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = AuthenticationRepositoryImp(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            preferences = sharedPreferences,
            networkInfo = networkInfo,
        )
    }

    private val tLoginParams = AuthenticationRequestEntity(userName = "test", password = "123456")
    private val loginResponse = fixture("login.json").run {
        Gson().fromJson(this, AuthenticationResponseModel::class.java)
    }
    private val tSignUpParams = SignUpRequestEntity(
        username = "test",
        email = "test@gmail.com",
        firstName = "jino",
        lastName = "pero",
        password = "123456",
    )
    private val tEmail = "test@gmail.com"
    private val messageResponse = fixture("message.json").run {
        Gson().fromJson(this, MessageResponseModel::class.java)
    }
    private val tUserEntity = UserMapper.mapToEntity(loginResponse)
    private val tAuthenticationResponseEntity =
        AuthenticationMapper.mapToEntity(model = loginResponse)
    private val tMessageResponseEntity = MessageResponseMapper.mapToEntity(model = messageResponse)
    private val token = loginResponse.token
    private val tEmailRequestEntity = EmailRequestEntity(email = "test@gmail.com")

    @Test
    fun `login should return Response of user when the internet is online `(): Unit = runTest {

        `when`(networkInfo.hasConnection()).thenReturn(true)
        `when`(remoteDataSource.login(loginParams = tLoginParams)).thenReturn(loginResponse)
        `when`(localDataSource.insertUser(userEntity = tUserEntity)).thenReturn(Unit)
        `when`(sharedPreferences.saveToken(token = token)).thenReturn(Unit)
        val result = repository.login(loginParams = tLoginParams)
        assertEquals(tAuthenticationResponseEntity, result)
        verify(networkInfo).hasConnection()
        verify(remoteDataSource).login(loginParams = tLoginParams)
        verify(localDataSource).insertUser(userEntity = tUserEntity)
        verify(sharedPreferences).saveToken(token = token)
    }

    @Test
    fun `login should throw ConnectionFailure when the internet is offline`(): Unit = runTest {
        `when`(networkInfo.hasConnection()).thenReturn(false)
        val result = assertFailsWith<Failures.ConnectionFailure> {
            repository.login(loginParams = tLoginParams)
        }
        assertEquals("No Internet Connection", result.message)
    }

    @Test
    fun `login should throw CacheFailure when insertUser and saveToken are fails `(): Unit =
        runTest {
            `when`(networkInfo.hasConnection()).thenReturn(true)
            `when`(remoteDataSource.login(loginParams = tLoginParams)).thenReturn(loginResponse)
            `when`(localDataSource.insertUser(userEntity = tUserEntity)).thenThrow(
                FailureException(
                    "Cache error"
                )
            )
            `when`(sharedPreferences.saveToken(token = token)).thenThrow(FailureException("Cache error"))
            val result = assertFailsWith<Failures.CacheFailure> {
                repository.login(loginParams = tLoginParams)
            }
            assertEquals("Cache error", result.message)
        }

    @Test
    fun `login should throw ServerFailure when the login is fails`(): Unit = runTest {
        `when`(networkInfo.hasConnection()).thenReturn(true)
        `when`(remoteDataSource.login(loginParams = tLoginParams)).thenThrow(FailureException("server error"))
        val result = assertFailsWith<Failures.ServerFailure> {
            repository.login(loginParams = tLoginParams)
        }
        assertEquals("server error", result.message)
    }

    @Test
    fun `signUp should return Response of message when the internet is online `(): Unit = runTest {
        `when`(networkInfo.hasConnection()).thenReturn(true)
        `when`(remoteDataSource.signUp(signUpParams = tSignUpParams)).thenReturn(messageResponse)
        val result = repository.signUp(tSignUpParams)
        assertEquals(tMessageResponseEntity, result)
        verify(networkInfo).hasConnection()
        verify(remoteDataSource).signUp(signUpParams = tSignUpParams)
    }

    @Test
    fun `signUp should throw ConnectionFailure when the internet is offline`(): Unit = runTest {
        `when`(networkInfo.hasConnection()).thenReturn(false)
        val result = assertFailsWith<Failures.ConnectionFailure> {
            repository.signUp(singUpParams = tSignUpParams)
        }
        assertEquals("No Internet Connection", result.message)
    }

    @Test
    fun `signUp should throw ServerFailure when the signUp is fails`(): Unit = runTest {
        `when`(networkInfo.hasConnection()).thenReturn(true)
        `when`(remoteDataSource.signUp(signUpParams = tSignUpParams)).thenThrow(FailureException("server error"))
        val result = assertFailsWith<Failures.ServerFailure> {
            repository.signUp(singUpParams = tSignUpParams)
        }
        assertEquals("server error", result.message)
    }

    @Test
    fun `resetPassword should return Response of message when the internet is online `(): Unit =
        runTest {
            `when`(networkInfo.hasConnection()).thenReturn(true)
            `when`(remoteDataSource.resetPassword(resetPasswordParams = tEmailRequestEntity)).thenReturn(messageResponse)
            val result = repository.resetPassword(tEmailRequestEntity)
            assertEquals(tMessageResponseEntity, result)
            verify(networkInfo).hasConnection()
            verify(remoteDataSource).resetPassword(resetPasswordParams = tEmailRequestEntity)
        }

    @Test
    fun `resetPassword should throw ConnectionFailure when the internet is offline`(): Unit =
        runTest {
            `when`(networkInfo.hasConnection()).thenReturn(false)
            val result = assertFailsWith<Failures.ConnectionFailure> {
                repository.resetPassword(tEmailRequestEntity)
            }
            assertEquals("No Internet Connection", result.message)
        }

    @Test
    fun `resetPassword should throw ServerFailure when the resetPassword is fails`(): Unit =
        runTest {
            `when`(networkInfo.hasConnection()).thenReturn(true)
            `when`(remoteDataSource.resetPassword(resetPasswordParams = tEmailRequestEntity)).thenThrow(FailureException("server error"))
            val result = assertFailsWith<Failures.ServerFailure> {
                repository.resetPassword(tEmailRequestEntity)
            }
            assertEquals("server error", result.message)
        }

}