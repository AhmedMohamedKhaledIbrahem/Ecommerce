package com.example.ecommerce.features.userprofile.data.datasources.localdatasource

import com.example.ecommerce.core.database.data.dao.user.UserDao
import com.example.ecommerce.core.database.data.entities.user.UserEntity
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.userprofile.data.models.UpdateUserNameDetailsResponseModel
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
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class UserProfileLocalDataSourceImpTest {
    @Mock
    private lateinit var userDao: UserDao
    private lateinit var localDataSource: UserProfileLocalDataSourceImp
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        localDataSource = UserProfileLocalDataSourceImp(dao = userDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val tUpdateUserNameDetailsResponseModel = UpdateUserNameDetailsResponseModel(
        id = 1,
        firstName = "test",
        lastName = "test",
        displayName = "test test"
    )
    private val tUserEntity = UserEntity(
        id = 2,
        userId = 1,
        firstName = "old_test",
        lastName = "old_test",
        displayName = "old_test old_test",
        userEmail = "test@gmail.com",
        userName = "test123",
        roles = "customer",
        verificationStatues = true,
        expiredToken = 12345,
        imagePath = ""
    )
    private val tUserId = 1
    private val tImage = ""

    @Test
    fun `updateUserNameDetails should update user when user exists`(): Unit = runTest {
        `when`(userDao.getUserById(userId = tUserId)).thenReturn(tUserEntity)
        localDataSource.updateUserNameDetails(
            updateUserNameDetailsParams = tUpdateUserNameDetailsResponseModel,
        )
        verify(userDao).updateUser(tUserEntity.apply {
            firstName = tUpdateUserNameDetailsResponseModel.firstName ?: ""
            lastName = tUpdateUserNameDetailsResponseModel.lastName ?: ""
            displayName = tUpdateUserNameDetailsResponseModel.displayName ?: ""
        })

    }

    @Test
    fun `updateUserNameDetails should throw FailureException when user is null `() = runTest {
        `when`(userDao.getUserById(userId = tUserId)).thenReturn(null)
        assertFailsWith<FailureException>("User is not found") {
            localDataSource.updateUserNameDetails(
                updateUserNameDetailsParams = tUpdateUserNameDetailsResponseModel
            )
        }
    }

    @Test
    fun `updateUserNameDetails should throw FailureException when dao getUserById  throw exception `() =
        runTest {
            val tErrorString = "Database error"
            `when`(userDao.getUserById(userId = tUserId)).thenThrow(RuntimeException(tErrorString))
            val exception = assertFailsWith<FailureException> {
                localDataSource.updateUserNameDetails(
                    updateUserNameDetailsParams = tUpdateUserNameDetailsResponseModel
                )
            }
            assertEquals(tErrorString, exception.message)
        }

    @Test
    fun `updateUserNameDetails should throw FailureException when dao updateUser  throw exception `() =
        runTest {
            val tErrorString = "update failed"
            `when`(userDao.getUserById(userId = tUserId)).thenReturn(tUserEntity)
            `when`(userDao.updateUser(userEntity = tUserEntity)).thenThrow(
                RuntimeException(
                    tErrorString
                )
            )
            val exception = assertFailsWith<FailureException> {
                localDataSource.updateUserNameDetails(
                    updateUserNameDetailsParams = tUpdateUserNameDetailsResponseModel
                )
            }
            assertEquals(tErrorString, exception.message)
        }

    @Test
    fun `getUserProfile should return User when saved in room`(): Unit = runTest {
        `when`(userDao.getUser()).thenReturn(tUserEntity)
        val result = localDataSource.getUserProfile()
        assertEquals(tUserEntity, result)
        verify(userDao).getUser()
    }

    @Test
    fun `getUserProfile should throw Failure Exception dao getUserProfile throw exception`(): Unit =
        runTest {
            val tErrorString = "get user information error"
            `when`(userDao.getUser()).thenThrow(RuntimeException(tErrorString))
            val exception = assertFailsWith<FailureException> {
                localDataSource.getUserProfile()
            }
            assertEquals(tErrorString, exception.message)

        }


    @Test
    fun `updateImageUserProfile should update Image user Profile when user exists`(): Unit =
        runTest {
            `when`(userDao.getUserById(userId = tUserId)).thenReturn(tUserEntity)
            localDataSource.updateImageUserProfile(userId = tUserId, image = tImage)
            verify(userDao).updateUser(tUserEntity.apply { imagePath = tImage })

        }

    @Test
    fun `updateImageUserProfile should throw FailureException when user is null `() = runTest {
        `when`(userDao.getUserById(userId = tUserId)).thenReturn(null)
        assertFailsWith<FailureException>("User is not found") {
            localDataSource.updateImageUserProfile(userId = tUserId, image = tImage)
        }
    }

    @Test
    fun `updateImageUserProfile should throw FailureException when dao updateUser throw exception `() =
        runTest {
            val tErrorString = "Database error"
            `when`(userDao.getUserById(userId = tUserId)).thenThrow(RuntimeException(tErrorString))
            val exception = assertFailsWith<FailureException> {
                localDataSource.updateImageUserProfile(userId = tUserId, image = tImage)
            }
            assertEquals(tErrorString, exception.message)
        }

    @Test
    fun `updateImageUserProfile should throw FailureException when dao updateUser  throw exception `() =
        runTest {
            val tErrorString = "update failed"
            `when`(userDao.getUserById(userId = tUserId)).thenReturn(tUserEntity)
            `when`(userDao.updateUser(userEntity = tUserEntity)).thenThrow(
                RuntimeException(
                    tErrorString
                )
            )
            val exception = assertFailsWith<FailureException> {
                localDataSource.updateImageUserProfile(userId = tUserId, image = tImage)
            }
            assertEquals(tErrorString, exception.message)
        }


}