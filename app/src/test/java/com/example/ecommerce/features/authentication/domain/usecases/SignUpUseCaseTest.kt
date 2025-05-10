package com.example.ecommerce.features.authentication.domain.usecases

import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import com.example.ecommerce.features.authentication.domain.usecases.signup.SignUpUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class SignUpUseCaseTest {
    @Mock
   private lateinit var repository: AuthenticationRepository
   private lateinit var signUpUseCase: SignUpUseCase

   @Before
   fun setUp(){
       MockitoAnnotations.openMocks(this)
       signUpUseCase = SignUpUseCase(repository = repository)
   }
   @Test
   fun`invoke should return Message from the repository`():Unit = runTest {
       val tSignUpParams = SignUpRequestEntity(
           username = "test",
           email = "test@gmail.com" ,
           firstName = "jino",
           lastName = "pero",
           password = "123456",
       )
       val tSignUpResponse = MessageResponseEntity(message = "sign up successful",true)
       `when`(repository.signUp(singUpParams = tSignUpParams)).thenReturn(tSignUpResponse)
       val result = signUpUseCase(signUpParams = tSignUpParams)
       assertEquals(tSignUpResponse,result)
       verify(repository).signUp(singUpParams = tSignUpParams)
       verifyNoMoreInteractions(repository)
   }
}