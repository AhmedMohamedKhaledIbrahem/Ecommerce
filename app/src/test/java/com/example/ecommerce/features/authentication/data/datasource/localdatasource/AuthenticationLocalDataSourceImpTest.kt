package com.example.ecommerce.features.authentication.data.datasource.localdatasource

import com.example.ecommerce.core.database.data.dao.user.UserDao
import com.example.ecommerce.core.database.data.mapper.UserMapper
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.authentication.data.datasources.localdatasource.AuthenticationLocalDataSourceImp
import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class AuthenticationLocalDataSourceImpTest {
    @Mock
    private lateinit var dao: UserDao
    private lateinit var localDataSource: AuthenticationLocalDataSourceImp


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        localDataSource = AuthenticationLocalDataSourceImp(dao = dao)
    }

    private val loginResponse = fixture("login.json").run {
        Gson().fromJson(this, AuthenticationResponseModel::class.java)
    }
    private val tUserEntity = UserMapper.mapToEntity(loginResponse)

    @Test
    fun `should call dao insertUser and cache data is successful`(): Unit = runTest {
        `when`(dao.insertUser(tUserEntity)).thenReturn(Unit)
        localDataSource.insertUser(tUserEntity)
        verify(dao).insertUser(tUserEntity)
    }

    @Test
    fun `should throw FailureException on dao `(): Unit = runTest {
        `when`(dao.insertUser(tUserEntity)).thenThrow(RuntimeException("database error"))
        val result = assertFailsWith<FailureException> {
            localDataSource.insertUser(tUserEntity)
        }
        assertEquals("database error", result.message)
    }

}