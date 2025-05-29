package com.example.ecommerce.features.authentication.data.repositories

import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.user.UserEntity
import com.example.ecommerce.core.database.data.mapper.UserMapper
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.authentication.data.datasources.localdatasource.AuthenticationLocalDataSource
import com.example.ecommerce.features.authentication.data.datasources.localdatasource.AuthenticationSharedPreferencesDataSource
import com.example.ecommerce.features.authentication.data.datasources.remotedatasource.AuthenticationRemoteDataSource
import com.example.ecommerce.features.authentication.data.mapper.AuthenticationMapper
import com.example.ecommerce.features.authentication.data.mapper.CheckVerificationRequestMapper
import com.example.ecommerce.features.authentication.data.mapper.EmailRequestMapper
import com.example.ecommerce.features.authentication.data.mapper.MessageResponseMapper
import com.example.ecommerce.features.authentication.data.mapper.SignUpRequestMapper
import com.example.ecommerce.features.authentication.data.mapper.toModel
import com.example.ecommerce.features.authentication.data.models.AuthenticationRequestModel
import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel
import com.example.ecommerce.features.authentication.data.models.ChangePasswordRequestModel
import com.example.ecommerce.features.authentication.data.models.CheckVerificationRequestModel
import com.example.ecommerce.features.authentication.data.models.ConfirmPasswordResetRequestModel
import com.example.ecommerce.features.authentication.data.models.EmailRequestModel
import com.example.ecommerce.features.authentication.data.models.MessageResponseModel
import com.example.ecommerce.features.authentication.data.models.SignUpRequestModel
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.ChangePasswordRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.CheckVerificationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.ConfirmPasswordResetRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.features.cacheFailureMessage
import com.example.ecommerce.features.connectionFailure
import com.example.ecommerce.features.serverFailure
import com.example.ecommerce.features.serverFailureMessage
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthenticationRepositoryImpTest {

    private val localDataSource: AuthenticationLocalDataSource =
        mockk<AuthenticationLocalDataSource>()


    private val remoteDataSource: AuthenticationRemoteDataSource =
        mockk<AuthenticationRemoteDataSource>()

    private val sharedPreferences: AuthenticationSharedPreferencesDataSource =
        mockk<AuthenticationSharedPreferencesDataSource>()

    private val networkInfo: InternetConnectionChecker = mockk<InternetConnectionChecker>()
    private lateinit var repository: AuthenticationRepositoryImp

    @Before
    fun setUp() {
        mockkObject(UserMapper)
        mockkObject(AuthenticationMapper)
        mockkObject(SignUpRequestMapper)
        mockkObject(EmailRequestMapper)
        mockkObject(CheckVerificationRequestMapper)
        mockkObject(MessageResponseMapper)
        mockkStatic("com.example.ecommerce.features.authentication.data.mapper.ConfirmPasswordChangeMapperKt")
        mockkStatic("com.example.ecommerce.features.authentication.data.mapper.ChangePasswordMapperKt")

        repository = AuthenticationRepositoryImp(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            preferences = sharedPreferences,
            networkInfo = networkInfo,
        )
    }

    private val authenticationRequestEntity = mockk<AuthenticationRequestEntity>()
    private val signUpRequestEntity = mockk<SignUpRequestEntity>()
    private val tEmailRequestEntity = mockk<EmailRequestEntity>()
    private val authenticationRequestModel = mockk<AuthenticationRequestModel>()
    private val tEmailParams = mockk<EmailRequestModel>()
    private val tSignUpParams = mockk<SignUpRequestModel>()
    private val tUserEntity = mockk<UserEntity>()
    private val tAuthenticationResponseEntity = mockk<AuthenticationResponseEntity>()
    private val tMessageResponseEntity = mockk<MessageResponseEntity>()
    private val tCheckVerificationRequestEntity = mockk<CheckVerificationRequestEntity>()
    private val tCheckVerificationRequestModel = mockk<CheckVerificationRequestModel>()
    private val tConfirmPasswordResetRequestEntity = mockk<ConfirmPasswordResetRequestEntity>()
    private val tConfirmPasswordResetRequestModel = mockk<ConfirmPasswordResetRequestModel>()
    private val tChangePasswordRequestEntity = mockk<ChangePasswordRequestEntity>()
    private val tChangePasswordRequestModel = mockk<ChangePasswordRequestModel>()
    private val loginResponse = fixture("login.json").run {
        Gson().fromJson(this, AuthenticationResponseModel::class.java)
    }
    private val messageResponse = fixture("message.json").run {
        Gson().fromJson(this, MessageResponseModel::class.java)
    }
    private val token = loginResponse.token


    @Test
    fun `login should return Response of user when the internet is online `(): Unit = runTest {

        coEvery { networkInfo.hasConnection() } returns true
        every { AuthenticationMapper.mapToModel(authenticationRequestEntity) } returns authenticationRequestModel
        coEvery { remoteDataSource.login(loginParams = authenticationRequestModel) } returns loginResponse
        coEvery { localDataSource.checkUserEntityById(loginResponse.userId) } returns null
        coEvery { sharedPreferences.saveToken(token = token) } just Runs
        every { UserMapper.mapToEntity(loginResponse) } returns tUserEntity
        coEvery { localDataSource.insertUser(userEntity = tUserEntity) } just Runs
        every { AuthenticationMapper.mapToEntity(loginResponse) } returns tAuthenticationResponseEntity
        val result = repository.login(loginParams = authenticationRequestEntity)
        assertEquals(tAuthenticationResponseEntity, result)
        coVerify(exactly = 1) { networkInfo.hasConnection() }
        coVerify(exactly = 1) { remoteDataSource.login(loginParams = authenticationRequestModel) }
        coVerify(exactly = 1) { localDataSource.checkUserEntityById(loginResponse.userId) }
        coVerify(exactly = 1) { sharedPreferences.saveToken(token = token) }
        coVerify(exactly = 1) { localDataSource.insertUser(userEntity = tUserEntity) }


    }

    @Test
    fun `login should throw ConnectionFailure when the internet is offline`(): Unit = runTest {
        coEvery { networkInfo.hasConnection() } returns false
        val result = connectionFailure {
            repository.login(loginParams = authenticationRequestEntity)
        }
        assertEquals(R.string.no_internet_connection, result.resourceId)

    }

    @Test
    fun `login should throw CacheFailure when insertUser and saveToken are fails `(): Unit =
        runTest {
            coEvery { networkInfo.hasConnection() } returns true
            every { AuthenticationMapper.mapToModel(authenticationRequestEntity) } returns authenticationRequestModel
            coEvery { remoteDataSource.login(loginParams = authenticationRequestModel) } returns loginResponse
            coEvery { localDataSource.checkUserEntityById(loginResponse.userId) } returns null
            coEvery { sharedPreferences.saveToken(token = token) } just Runs
            every { UserMapper.mapToEntity(loginResponse) } returns tUserEntity
            coEvery { localDataSource.insertUser(userEntity = tUserEntity) } throws FailureException(
                cacheFailureMessage
            )
            val result = assertFailsWith<Failures.CacheFailure> {
                repository.login(loginParams = authenticationRequestEntity)
            }
            assertEquals(cacheFailureMessage, result.message)
        }

    @Test
    fun `login should throw ServerFailure when the login is fails`(): Unit = runTest {
        coEvery { networkInfo.hasConnection() } returns true
        every { AuthenticationMapper.mapToModel(authenticationRequestEntity) } returns authenticationRequestModel
        coEvery { remoteDataSource.login(loginParams = authenticationRequestModel) } throws FailureException(
            serverFailureMessage
        )
        val result = serverFailure {
            repository.login(loginParams = authenticationRequestEntity)
        }
        assertEquals(serverFailureMessage, result.message)
    }

    @Test
    fun `signUp should return Response of message when the internet is online `(): Unit = runTest {
        coEvery { networkInfo.hasConnection() } returns true
        every { SignUpRequestMapper.mapToModel(signUpRequestEntity) } returns tSignUpParams
        coEvery { remoteDataSource.signUp(signUpParams = tSignUpParams) } returns messageResponse
        every { MessageResponseMapper.mapToEntity(messageResponse) } returns tMessageResponseEntity
        val result = repository.signUp(signUpRequestEntity)
        assertEquals(tMessageResponseEntity, result)
        coVerify(exactly = 1) { networkInfo.hasConnection() }
        coVerify(exactly = 1) { remoteDataSource.signUp(signUpParams = tSignUpParams) }
    }

    @Test
    fun `signUp should throw ConnectionFailure when the internet is offline`(): Unit = runTest {
        coEvery { networkInfo.hasConnection() } returns false
        val result = connectionFailure {
            repository.signUp(singUpParams = signUpRequestEntity)
        }
        assertEquals(R.string.no_internet_connection, result.resourceId)
    }

    @Test
    fun `signUp should throw ServerFailure when the signUp is fails`(): Unit = runTest {
        coEvery { networkInfo.hasConnection() } returns true
        every { SignUpRequestMapper.mapToModel(signUpRequestEntity) } returns tSignUpParams
        coEvery { remoteDataSource.signUp(signUpParams = tSignUpParams) } throws FailureException(
            serverFailureMessage
        )
        val result = serverFailure {
            repository.signUp(singUpParams = signUpRequestEntity)
        }
        assertEquals(serverFailureMessage, result.message)
    }

    @Test
    fun `resetPassword should return Response of message when the internet is online `(): Unit =
        runTest {
            coEvery { networkInfo.hasConnection() } returns true
            every { EmailRequestMapper.mapToModel(tEmailRequestEntity) } returns tEmailParams
            coEvery { remoteDataSource.resetPassword(resetPasswordParams = tEmailParams) } returns messageResponse
            repository.resetPassword(tEmailRequestEntity)
            coVerify(exactly = 1) { networkInfo.hasConnection() }
            coVerify(exactly = 1) { remoteDataSource.resetPassword(resetPasswordParams = tEmailParams) }
        }

    @Test
    fun `resetPassword should throw ConnectionFailure when the internet is offline`(): Unit =
        runTest {
            coEvery { networkInfo.hasConnection() } returns false
            val result = connectionFailure {
                repository.resetPassword(tEmailRequestEntity)
            }
            assertEquals(R.string.no_internet_connection, result.resourceId)
        }

    @Test
    fun `resetPassword should throw ServerFailure when the resetPassword is fails`(): Unit =
        runTest {
            coEvery { networkInfo.hasConnection() } returns true
            every { EmailRequestMapper.mapToModel(tEmailRequestEntity) } returns tEmailParams
            coEvery { remoteDataSource.resetPassword(resetPasswordParams = tEmailParams) } throws FailureException(
                serverFailureMessage
            )
            val result = serverFailure {
                repository.resetPassword(tEmailRequestEntity)
            }
            assertEquals(serverFailureMessage, result.message)
        }

    @Test
    fun `sendVerificationEmail should return Response of message when the internet is online `() =
        runTest {
            coEvery { networkInfo.hasConnection() } returns true
            every { EmailRequestMapper.mapToModel(tEmailRequestEntity) } returns tEmailParams
            coEvery { remoteDataSource.sendVerificationCode(sendVerificationCodeParams = tEmailParams) } returns messageResponse
            every { MessageResponseMapper.mapToEntity(messageResponse) } returns tMessageResponseEntity
            repository.sendVerificationCode(tEmailRequestEntity)
            coVerify(exactly = 1) { networkInfo.hasConnection() }
            coVerify(exactly = 1) { remoteDataSource.sendVerificationCode(sendVerificationCodeParams = tEmailParams) }

        }

    @Test
    fun `sendVerificationEmail should throw ConnectionFailure when the internet is offline`() =
        runTest {
            coEvery { networkInfo.hasConnection() } returns false
            val result = connectionFailure {
                repository.sendVerificationCode(tEmailRequestEntity)
            }
            assertEquals(R.string.no_internet_connection, result.resourceId)
        }

    @Test
    fun `sendVerificationEmail should throw ServerFailure when the sendVerificationCode is fails`() =
        runTest {
            coEvery { networkInfo.hasConnection() } returns true
            every { EmailRequestMapper.mapToModel(tEmailRequestEntity) } returns tEmailParams
            coEvery { remoteDataSource.sendVerificationCode(sendVerificationCodeParams = tEmailParams) } throws FailureException(
                serverFailureMessage
            )
            val result = serverFailure {
                repository.sendVerificationCode(tEmailRequestEntity)
            }
            assertEquals(serverFailureMessage, result.message)

        }

    @Test
    fun `checkVerificationCode should return Response of message when the internet is online `() =
        runTest {
            val email = "test@example.com"
            every {
                CheckVerificationRequestMapper.mapToModel(tCheckVerificationRequestEntity)
            } returns tCheckVerificationRequestModel
            every { tCheckVerificationRequestEntity.email } returns email
            every { MessageResponseMapper.mapToEntity(messageResponse) } returns tMessageResponseEntity

            coEvery { networkInfo.hasConnection() } returns true
            coEvery {
                remoteDataSource.checkVerificationCode(checkVerificationCodeParams = tCheckVerificationRequestModel)
            } returns messageResponse
            coEvery {
                localDataSource.updateVerificationStatusByEmail(
                    email,
                    true
                )
            } just Runs

            repository.checkVerificationCode(tCheckVerificationRequestEntity)

            coVerify(exactly = 1) {
                networkInfo.hasConnection()
            }
            coVerify(exactly = 1) {
                remoteDataSource.checkVerificationCode(checkVerificationCodeParams = tCheckVerificationRequestModel)
            }
            coVerify(exactly = 1) {
                localDataSource.updateVerificationStatusByEmail(
                    email,
                    true
                )
            }
        }

    @Test
    fun `checkVerificationCode should throw ConnectionFailure when the internet is offline`() =
        runTest {
            coEvery { networkInfo.hasConnection() } returns false
            val result = connectionFailure {
                repository.checkVerificationCode(tCheckVerificationRequestEntity)
            }
            assertEquals(R.string.no_internet_connection, result.resourceId)
        }

    @Test
    fun `checkVerificationCode should throw ServerFailure when the checkVerificationCode is fails`() =
        runTest {
            every {
                CheckVerificationRequestMapper.mapToModel(tCheckVerificationRequestEntity)
            } returns tCheckVerificationRequestModel
            coEvery { networkInfo.hasConnection() } returns true
            coEvery {
                remoteDataSource.checkVerificationCode(checkVerificationCodeParams = tCheckVerificationRequestModel)
            } throws FailureException(serverFailureMessage)
            val result = serverFailure {
                repository.checkVerificationCode(tCheckVerificationRequestEntity)
            }
            assertEquals(serverFailureMessage, result.message)
        }

    @Test
    fun `confirmResetPassword should return Response of message when the internet is online `() =
        runTest {

            every { tConfirmPasswordResetRequestEntity.toModel() } returns tConfirmPasswordResetRequestModel
            coEvery { networkInfo.hasConnection() } returns true
            coEvery {
                remoteDataSource.confirmPasswordChange(tConfirmPasswordResetRequestEntity.toModel())
            } returns messageResponse
            repository.confirmPasswordChange(tConfirmPasswordResetRequestEntity)
        }

    @Test
    fun `confirmResetPassword should throw ConnectionFailure when the internet is offline`() =
        runTest {
            coEvery { networkInfo.hasConnection() } returns false
            val result = connectionFailure {
                repository.confirmPasswordChange(tConfirmPasswordResetRequestEntity)
            }
            assertEquals(R.string.no_internet_connection, result.resourceId)
        }

    @Test
    fun `confirmResetPassword should throw ServerFailure when the confirmPasswordChange is fails`() =
        runTest {
            coEvery { networkInfo.hasConnection() } returns true
            every { tConfirmPasswordResetRequestEntity.toModel() } returns tConfirmPasswordResetRequestModel
            coEvery {
                remoteDataSource.confirmPasswordChange(tConfirmPasswordResetRequestEntity.toModel())
            } throws FailureException(serverFailureMessage)
            val result = serverFailure {
                repository.confirmPasswordChange(tConfirmPasswordResetRequestEntity)
            }
            assertEquals(serverFailureMessage, result.message)
        }

    @Test
    fun `changePassword should return Response of message when the internet is online `() =
        runTest {
            every { tChangePasswordRequestEntity.toModel() } returns tChangePasswordRequestModel
            coEvery { networkInfo.hasConnection() } returns true
            coEvery {
                remoteDataSource.changePassword(tChangePasswordRequestEntity.toModel())
            } returns messageResponse
            repository.changePassword(tChangePasswordRequestEntity)
            coVerify(exactly = 1) { networkInfo.hasConnection() }
            coVerify(exactly = 1) {
                remoteDataSource.changePassword(tChangePasswordRequestEntity.toModel())
            }
        }
    @Test
    fun `changePassword should throw ConnectionFailure when the internet is offline`() =runTest {
        coEvery { networkInfo.hasConnection() } returns false
        val result = connectionFailure {
            repository.changePassword(tChangePasswordRequestEntity)
        }
        assertEquals(R.string.no_internet_connection, result.resourceId)
    }
    @Test
    fun `changePassword should throw ServerFailure when the changePassword is fails` ()=runTest {
        every { tChangePasswordRequestEntity.toModel() } returns tChangePasswordRequestModel
        coEvery { networkInfo.hasConnection() } returns true
        coEvery {
            remoteDataSource.changePassword(tChangePasswordRequestEntity.toModel())
        } throws FailureException(serverFailureMessage)
        val result = serverFailure { repository.changePassword(tChangePasswordRequestEntity) }
        assertEquals(serverFailureMessage, result.message)


    }
}