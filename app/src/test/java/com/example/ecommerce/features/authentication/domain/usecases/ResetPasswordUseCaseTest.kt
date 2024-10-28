package com.example.ecommerce.features.authentication.domain.usecases

import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class ResetPasswordUseCaseTest {
    @Mock
    private lateinit var repository: AuthenticationRepository
    private lateinit var restPasswordUseCase: ResetPasswordUseCase

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        restPasswordUseCase = ResetPasswordUseCase(repository = repository)
    }
    @Test
    fun`invoke should return reset password from the repository`():Unit = runTest {
        val tEmail = "test@gmail.com"
        val tResetPasswordResponse = MessageResponseEntity("reset password send to your email")
        `when`(repository.resetPassword(tEmail)).thenReturn(tResetPasswordResponse)
        val result = restPasswordUseCase(email = tEmail)
        assertEquals(tResetPasswordResponse,result)
        verify(repository).resetPassword(email = tEmail)
        verifyNoMoreInteractions(repository)
    }
}