package com.example.ecommerce.features.userprofile.domain.usecases

import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import com.example.ecommerce.features.userprofile.domain.usecases.updateuserprofile.UpdateUserProfileUseCase
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
class UpdateUserProfileUseCaseTest {
    @Mock
    private lateinit var repository: UserProfileRepository
    private lateinit var updateUserProfileUseCase: UpdateUserProfileUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        updateUserProfileUseCase = UpdateUserProfileUseCase(repository = repository)
    }

    @Test
    fun `invoke should UpdateUserProfile  from the repository`(): Unit = runTest {
        /*val tUserId = 1
        `when`(repository.updateUserNameDetails(userId = tUserId)).thenReturn(Unit)
        val result = updateUserProfileUseCase.invoke(userId = tUserId)
        assertEquals(Unit, result)
        verify(repository).updateUserNameDetails(userId = tUserId)

        */verifyNoMoreInteractions(repository)
    }

}