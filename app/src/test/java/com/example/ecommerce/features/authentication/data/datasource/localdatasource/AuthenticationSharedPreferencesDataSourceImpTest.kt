package com.example.ecommerce.features.authentication.data.datasource.localdatasource

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.manager.token.TokenManager
import com.example.ecommerce.features.authentication.data.datasources.localdatasource.AuthenticationSharedPreferencesDataSourceImp
import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class AuthenticationSharedPreferencesDataSourceImpTest {
    @Mock
    private lateinit var tokenManger : TokenManager
    private lateinit var sharedPreferences:AuthenticationSharedPreferencesDataSourceImp

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        sharedPreferences = AuthenticationSharedPreferencesDataSourceImp(tokenManager = tokenManger)
    }
   private var userResponse = fixture("login.json").run {
        Gson().fromJson(this,AuthenticationResponseModel::class.java)
   }
   private var tToken = userResponse.token
   @Test
   fun`should save token from authenticationResponseModel is successful`():Unit= runTest {
       `when`(tokenManger.saveToken(tToken)).thenReturn(Unit)
       sharedPreferences.saveToken(tToken)
       verify(tokenManger).saveToken(tToken)
   }
   @Test
   fun`should throw FailureException when call tokenManger to saveToken is unsuccessful`():Unit = runTest {
       `when`(tokenManger.saveToken(tToken)).thenThrow(RuntimeException("save Token error"))
       val result = assertFailsWith<FailureException> {
           sharedPreferences.saveToken(tToken)
       }
       assertEquals("save Token error",result.message)
   }
   @Test
   fun`should clear token when is logout successful`():Unit = runTest {
       `when`(tokenManger.clearToken()).thenReturn(Unit)
       sharedPreferences.clearToken()
       verify(tokenManger).clearToken()
   }
    @Test
    fun`should throw FailureException when call tokenManger to clearToken is unsuccessful`():Unit = runTest {
        `when`(tokenManger.clearToken()).thenThrow(RuntimeException("clear Token error"))
        val result = assertFailsWith<FailureException> {
            sharedPreferences.clearToken()
        }
        assertEquals("clear Token error",result.message)
    }
}