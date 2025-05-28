package com.example.ecommerce.features.userprofile.domain.usecases

import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import com.example.ecommerce.features.userprofile.domain.usecases.fetch_update_user_details.FetchUpdateUserDetailsUseCase
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
class GetUserDetailsUseCaseTest {
    @Mock
    private lateinit var repository: UserProfileRepository
    private lateinit var fetchUpdateUserDetailsUseCase: FetchUpdateUserDetailsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        fetchUpdateUserDetailsUseCase = FetchUpdateUserDetailsUseCase(repository = repository)
    }

    @Test
    fun `should invoke getUserNameDetails from repository`(): Unit = runTest {
        val name = "ahmed"
        `when`(repository.fetchUpdateUserDetails()).thenReturn(name)
        val result = fetchUpdateUserDetailsUseCase.invoke()
        assertEquals(name, result)
        verify(repository).fetchUpdateUserDetails()
        verifyNoMoreInteractions(repository)
    }
}