package com.example.ecommerce.features.userprofile.domain.usecases

import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserNameDetailsRequestEntity
import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import com.example.ecommerce.features.userprofile.domain.usecases.updateusernamedetails.UpdateUserNameDetailsUseCase
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
class UpdateUserNameDetailsUseCaseTest {
    @Mock
    private lateinit var repository: UserProfileRepository
    private lateinit var updateUserNameDetailsUseCase: UpdateUserNameDetailsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        updateUserNameDetailsUseCase = UpdateUserNameDetailsUseCase(repository = repository)
    }

    private val tUserNameDetailsRequestEntity = UpdateUserNameDetailsRequestEntity(
        id = 1,
        firstName = "test",
        lastName = "test",
        displayName = "test test"
    )

    @Test
    fun `invoke should UpdateUserProfile  from the repository`(): Unit = runTest {
        `when`(
            repository.updateUserNameDetails(
                updateUserNameDetailsParams = tUserNameDetailsRequestEntity
            )
        ).thenReturn(Unit)
        val result = updateUserNameDetailsUseCase.invoke(
            updateUserNameDetailsParams = tUserNameDetailsRequestEntity
        )
        assertEquals(Unit,result)
        verify(repository).updateUserNameDetails(
            updateUserNameDetailsParams = tUserNameDetailsRequestEntity
        )
        verifyNoMoreInteractions(repository)
    }

}