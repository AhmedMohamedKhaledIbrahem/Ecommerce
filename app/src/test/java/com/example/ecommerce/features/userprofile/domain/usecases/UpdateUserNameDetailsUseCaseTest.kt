package com.example.ecommerce.features.userprofile.domain.usecases

import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserDetailsRequestEntity
import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import com.example.ecommerce.features.userprofile.domain.usecases.update_user_details.UpdateUserDetailsUseCase
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
    private lateinit var updateUserDetailsUseCase: UpdateUserDetailsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        updateUserDetailsUseCase = UpdateUserDetailsUseCase(repository = repository)
    }

    private val tUserNameDetailsRequestEntity = UpdateUserDetailsRequestEntity(
        id = 1,
        firstName = "test",
        lastName = "test",
        displayName = "test test"
    )

    @Test
    fun `invoke should UpdateUserProfile  from the repository`(): Unit = runTest {
        `when`(
            repository.updateUserDetails(
                updateUserDetailsParams = tUserNameDetailsRequestEntity
            )
        ).thenReturn(Unit)
        val result = updateUserDetailsUseCase.invoke(
            updateUserDetailsParams = tUserNameDetailsRequestEntity
        )
        assertEquals(Unit,result)
        verify(repository).updateUserDetails(
            updateUserDetailsParams = tUserNameDetailsRequestEntity
        )
        verifyNoMoreInteractions(repository)
    }

}