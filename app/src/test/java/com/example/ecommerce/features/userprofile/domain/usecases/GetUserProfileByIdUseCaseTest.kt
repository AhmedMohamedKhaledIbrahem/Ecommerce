package com.example.ecommerce.features.userprofile.domain.usecases

import com.example.ecommerce.core.data.entities.UserEntity
import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import com.example.ecommerce.features.userprofile.domain.usecases.getuserprofilebyid.GetUserProfileByIdUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetUserProfileByIdUseCaseTest {
    @Mock
    private lateinit var repository: UserProfileRepository
    private lateinit var getUserProfileByIdUseCase: GetUserProfileByIdUseCase

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        getUserProfileByIdUseCase = GetUserProfileByIdUseCase(repository = repository)
    }
    @Test
    fun `invoke should return UserProfile from the repository`(): Unit = runTest {
        val tGetUserProfileResponseEntity =
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
        val tUserId = 1
        `when`(repository.getUserProfile(userId = tUserId)).thenReturn(
            tGetUserProfileResponseEntity
        )
        val result = getUserProfileByIdUseCase.invoke(userId = tUserId)
        assertEquals(tGetUserProfileResponseEntity,result)
        verify(repository).getUserProfile(userId = tUserId)
        verifyNoMoreInteractions(repository)
    }
}