package com.example.ecommerce.features.authentication.domain.usecases

import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationResponseEntity
import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class LoginUseCaseTest {
    @Mock
    private lateinit var repository: AuthenticationRepository
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        loginUseCase = LoginUseCase(repository = repository)
    }

    @Test
    fun `invoke should return login from the repository`():Unit = runTest {
        val tLoginParams = AuthenticationRequestEntity(userName = "test@gmail.com" , password ="123456")
        val tLoginResponse = AuthenticationResponseEntity(
            userId = 5 ,
            token = "aspjdoshdo",
            userName = "test",
            userEmail = "test@gmail.com" ,
            firstName = "jino",
            lastName = "pero",
            displayName = "jino pero" ,
            roles = arrayListOf("customer"),
            expiredToken = 123456
        )
        `when`(repository.login(loginParams = tLoginParams)).thenReturn(tLoginResponse)
        val result = loginUseCase(loginParams = tLoginParams)
        assertEquals(tLoginResponse,result)
        verify(repository).login(loginParams = tLoginParams)
        verifyNoMoreInteractions(repository)
    }
}