package com.example.ecommerce.features.userprofile.domain.usecases

import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import com.example.ecommerce.features.userprofile.domain.usecases.getusernamedetails.GetUserNameDetailsUseCase
import io.mockk.verify
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
class GetUserNameDetailsUseCaseTest {
    @Mock
    private lateinit var repository: UserProfileRepository
    private lateinit var getUserNameDetailsUseCase: GetUserNameDetailsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getUserNameDetailsUseCase = GetUserNameDetailsUseCase(repository = repository)
    }

    @Test
    fun `should invoke getUserNameDetails from repository`(): Unit = runTest {
        `when`(repository.getUserNameDetails()).thenReturn(Unit)
        val result = getUserNameDetailsUseCase.invoke()
        assertEquals(Unit, result)
        verify(repository).getUserNameDetails()
        verifyNoMoreInteractions(repository)
    }
}