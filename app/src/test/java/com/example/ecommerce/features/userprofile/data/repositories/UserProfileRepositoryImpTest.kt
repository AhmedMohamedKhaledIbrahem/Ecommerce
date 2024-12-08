package com.example.ecommerce.features.userprofile.data.repositories

import com.example.ecommerce.core.data.entities.UserEntity
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.userprofile.data.datasources.localdatasource.UserProfileLocalDataSource
import com.example.ecommerce.features.userprofile.data.datasources.remotedatasource.UserProfileRemoteDataSource
import com.example.ecommerce.features.userprofile.data.mapper.GetImageProfileMapper
import com.example.ecommerce.features.userprofile.data.mapper.UploadImageProfileMapper
import com.example.ecommerce.features.userprofile.data.models.CheckUserNameDetailsResponseModel
import com.example.ecommerce.features.userprofile.data.models.GetImageProfileResponseModel
import com.example.ecommerce.features.userprofile.data.models.UpdateUserNameDetailsRequestModel
import com.example.ecommerce.features.userprofile.data.models.UpdateUserNameDetailsResponseModel
import com.example.ecommerce.features.userprofile.data.models.UploadImageProfileResponseModel
import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserNameDetailsRequestEntity
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class UserProfileRepositoryImpTest {
    @Mock
    private lateinit var localDataSource: UserProfileLocalDataSource

    @Mock
    private lateinit var remoteDataSource: UserProfileRemoteDataSource

    @Mock
    private lateinit var internetConnectionChecker: InternetConnectionChecker

    private lateinit var repository: UserProfileRepositoryImp
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        repository = UserProfileRepositoryImp(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            internetConnectionChecker = internetConnectionChecker
        )
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
    private val tUpdateUserNameDetailsRequestEntity = UpdateUserNameDetailsRequestEntity(
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
    private val tGetImageProfileResponseEntity = GetImageProfileMapper.mapToEntity(
        model = tGetImageProfileResponseModel
    )
    private val tUploadImageProfileResponseModel = fixture("uploadImageProfile.json").run {
        Gson().fromJson(this, UploadImageProfileResponseModel::class.java)
    }
    private val tTempFile: File = File.createTempFile("test_image", ".jpg").apply {
        writeText("dummy content")
    }
    private val tUploadImageProfileResponseEntity =
        UploadImageProfileMapper.mapToEntity(tUploadImageProfileResponseModel)
    private val tGetUserProfileResponseEntity =
        UserEntity(
            userId = 1,
            userName = "test",
            userEmail = "test@gmail.com",
            firstName = "test",
            lastName = "test2",
            displayName = "test test2",
            imagePath = "",
            expiredToken = 123,
            verificationStatues = true,
            id = 1,
            roles = "customer"
        )
    private val tUserId = 1
    private val tImage = ""
    private val connectionFailure = "No Internet Connection"
    private val serverFailure = "Server error"
    private val cacheFailure = "Cache error"

    @Test
    fun `getImageProfileById should return GetImageProfileResponseEntity when internet connection is available`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
            `when`(remoteDataSource.getImageProfileById(userId = tUserId)).thenReturn(
                tGetImageProfileResponseModel
            )
            `when`(
                localDataSource.updateImageUserProfile(
                    userId = tUserId,
                    image = tImage
                )
            ).thenReturn(Unit)
            val result = repository.getImageProfileById(userId = tUserId)
            assertEquals(tGetImageProfileResponseEntity, result)
            verify(localDataSource).updateImageUserProfile(
                userId = result.userId,
                image = result.profileImage
            )
        }


    @Test
    fun `getImageProfileById should throw ConnectionFailure when internet connection is not available`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(false)
            val exception = assertFailsWith<Failures.ConnectionFailure> {
                repository.getImageProfileById(userId = tUserId)
            }
            assertEquals(connectionFailure, exception.message)

        }

    @Test
    fun `getImageProfileById should throw ServerFailure when remoteDataSource throws exception`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
            `when`(remoteDataSource.getImageProfileById(userId = tUserId)).thenThrow(
                FailureException(serverFailure)
            )
            val exception = assertFailsWith<Failures.ServerFailure> {
                repository.getImageProfileById(userId = tUserId)
            }
            assertEquals(serverFailure, exception.message)
        }

    @Test
    fun `getUserProfile should return UserEntity when localDataSource is successful`() = runTest {
        `when`(localDataSource.getUserProfile()).thenReturn(tGetUserProfileResponseEntity)
        val result = repository.getUserProfile()
        assertEquals(tGetUserProfileResponseEntity, result)

    }

    @Test
    fun `getUserProfile should throw CacheFailure when localDataSource throws exception`() =
        runTest {
            `when`(localDataSource.getUserProfile()).thenThrow(FailureException(cacheFailure))
            val exception = assertFailsWith<Failures.CacheFailure> {
                repository.getUserProfile()
            }
            assertEquals(cacheFailure, exception.message)

        }

    @Test
    fun `getUserNameDetails should return Unit when internet connection is available`() = runTest {
        `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
        `when`(remoteDataSource.checkUserNameDetailsUpdate()).thenReturn(
            tCheckUserNameDetailsResponseModel
        )
        val result = repository.getUserNameDetails()
        assertEquals(Unit, result)


    }

    @Test
    fun `getUserNameDetails should throw ConnectionFailure when internet connection is not available`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(false)
            val exception = assertFailsWith<Failures.ConnectionFailure> {
                repository.getUserNameDetails()
            }
            assertEquals(connectionFailure, exception.message)
        }

    @Test
    fun `getUserNameDetails should throw ServerFailure when remoteDataSource throws exception`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
            `when`(remoteDataSource.checkUserNameDetailsUpdate()).thenThrow(
                FailureException(
                    serverFailure
                )
            )
            val exception = assertFailsWith<Failures.ServerFailure> {
                repository.getUserNameDetails()
            }
            assertEquals(serverFailure, exception.message)
        }

    @Test
    fun `uploadImageProfile should return UploadImageProfileResponseEntity when internet connection is available`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
            `when`(remoteDataSource.uploadImageProfile(image = tTempFile)).thenReturn(
                tUploadImageProfileResponseModel
            )
            val result = repository.uploadImageProfile(image = tTempFile)
            assertEquals(tUploadImageProfileResponseEntity, result)
            verify(localDataSource).updateImageUserProfile(
                userId = result.userid,
                image = result.imageLink
            )

        }

    @Test
    fun `uploadImageProfile should throw ConnectionFailure when internet connection is not available`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(false)
            val exception = assertFailsWith<Failures.ConnectionFailure> {
                repository.uploadImageProfile(image = tTempFile)
            }
            assertEquals(connectionFailure, exception.message)
        }

    @Test
    fun `uploadImageProfile should throw ServerFailure when remoteDataSource throws exception`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
            `when`(remoteDataSource.uploadImageProfile(image = tTempFile)).thenThrow(
                FailureException(
                    serverFailure
                )
            )
            val exception = assertFailsWith<Failures.ServerFailure> {
                repository.uploadImageProfile(image = tTempFile)
            }
            assertEquals(serverFailure, exception.message)
        }

    @Test
    fun `updateUserNameDetails should return Unit when internet connection is available`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
            `when`(
                remoteDataSource.updateUserNameDetails(
                    updateUserNameDetailsParams = tUpdateUserNameDetailsRequestModel
                )
            ).thenReturn(tUpdateUserNameDetailsResponseModel)
            val result = repository.updateUserNameDetails(
                updateUserNameDetailsParams = tUpdateUserNameDetailsRequestEntity
            )
            assertEquals(Unit, result)

        }

    @Test
    fun `updateUserNameDetails should throw ConnectionFailure when internet connection is not available`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(false)
            val exception = assertFailsWith<Failures.ConnectionFailure> {
                repository.updateUserNameDetails(
                    updateUserNameDetailsParams = tUpdateUserNameDetailsRequestEntity
                )

            }
            assertEquals(connectionFailure, exception.message)
        }

    @Test
    fun `updateUserNameDetails should throw ServerFailure when remoteDataSource throws exception`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
            `when`(
                remoteDataSource.updateUserNameDetails(
                    updateUserNameDetailsParams = tUpdateUserNameDetailsRequestModel
                )
            ).thenThrow(FailureException(serverFailure))
            val exception = assertFailsWith<Failures.ServerFailure> {
                repository.updateUserNameDetails(
                    updateUserNameDetailsParams = tUpdateUserNameDetailsRequestEntity
                )

            }
            assertEquals(serverFailure, exception.message)
        }
}