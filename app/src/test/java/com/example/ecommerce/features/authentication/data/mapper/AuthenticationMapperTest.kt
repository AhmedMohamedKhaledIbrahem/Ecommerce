package com.example.ecommerce.features.authentication.data.mapper

import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationResponseEntity
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import org.junit.Test
import kotlin.test.assertEquals

class AuthenticationMapperTest {
    private val tAuthenticationResponseModel = AuthenticationResponseModel(
        token = "token",
        email = "test@gmail.com",
        userName = "test",
        userDisplayName = "jino pero",
        roles = arrayListOf("customer"),
        userId = 1,
        firstName = "jino",
        lastName = "pero",
        verificationStatues = false,
        expiredToken = 1
    )
    private val tAuthenticationResponseEntity = AuthenticationResponseEntity(
        token = "token",
        userEmail = "test@gmail.com",
        userName = "test",
        displayName = "jino pero",
        roles = arrayListOf("customer"),
        userId = 1,
        firstName = "jino",
        lastName = "pero",
        verificationStatues = false,
        expiredToken = 1,
    )

    @Test
    fun `load json and parse as AuthenticationResponseModel object`() {
        val json = fixture("login.json")
        val result: AuthenticationResponseModel =
            Gson().fromJson(json, AuthenticationResponseModel::class.java)
        assertEquals(tAuthenticationResponseModel,result)

    }
    @Test
    fun `should map AuthenticationResponseModel to the AuthenticationResponseEntity`(){
        val result = AuthenticationMapper.mapToEntity(tAuthenticationResponseModel)
        assertEquals(expected = tAuthenticationResponseEntity, actual = result)
    }
}