package com.example.ecommerce.features.authentication.domain.usecases

import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import com.example.ecommerce.features.authentication.domain.usecases.restpassword.ResetPasswordUseCase
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
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        restPasswordUseCase = ResetPasswordUseCase(repository = repository)
    }

    private val tEmailRequestEntity = EmailRequestEntity(email = "test@gmail.com")

    @Test
    fun `invoke should return reset password from the repository`(): Unit = runTest {
        val tResetPasswordResponse =
            MessageResponseEntity("reset password send to your email", true)
        `when`(repository.resetPassword(tEmailRequestEntity)).thenReturn(tResetPasswordResponse)
        val result = restPasswordUseCase(resetPasswordParams = tEmailRequestEntity)
        assertEquals(tResetPasswordResponse, result)
        verify(repository).resetPassword(resetPasswordParams = tEmailRequestEntity)
        verifyNoMoreInteractions(repository)
    }
}