package com.example.ecommerce.features.userprofile.domain.usecases

import com.example.ecommerce.core.data.entities.user.UserEntity
import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import com.example.ecommerce.features.userprofile.domain.usecases.getuserprofilebyid.GetUserProfileUseCase
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
class GetUserProfileUseCaseTest {
    @Mock
    private lateinit var repository: UserProfileRepository
    private lateinit var getUserProfileUseCase: GetUserProfileUseCase

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        getUserProfileUseCase = GetUserProfileUseCase(repository = repository)
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
        `when`(repository.getUserProfile()).thenReturn(
            tGetUserProfileResponseEntity
        )
        val result = getUserProfileUseCase.invoke()
        assertEquals(tGetUserProfileResponseEntity,result)
        verify(repository).getUserProfile()
        verifyNoMoreInteractions(repository)
    }
}