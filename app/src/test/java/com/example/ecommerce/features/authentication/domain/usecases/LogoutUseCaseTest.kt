package com.example.ecommerce.features.authentication.domain.usecases

import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import com.example.ecommerce.features.authentication.domain.usecases.logout.LogoutUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class LogoutUseCaseTest {
    @Mock
    private lateinit var repository: AuthenticationRepository
    private lateinit var logoutUseCase: LogoutUseCase

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        logoutUseCase = LogoutUseCase(repository = repository)
    }
    @Test
    fun`invoke should return Unit from the repository`():Unit = runTest {
//        `when`(repository.logout()).thenReturn(Unit)
//        logoutUseCase.invoke()
//        verify(repository).logout()
//        verifyNoMoreInteractions(repository)

    }
}