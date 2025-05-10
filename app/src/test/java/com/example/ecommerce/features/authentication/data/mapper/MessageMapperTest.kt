package com.example.ecommerce.features.authentication.data.mapper

import com.example.ecommerce.features.authentication.data.models.MessageResponseModel
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import org.junit.Test
import kotlin.test.assertEquals

class MessageMapperTest {
   private val tMessageResponseModel = MessageResponseModel(message = "test",true)
   private val tMessageResponseEntity = MessageResponseEntity(message = "test",true)

    @Test
    fun`should json parse as MessageResponseModel`(){
        val json = fixture("message.json")
        val result :MessageResponseModel = Gson().fromJson(json,MessageResponseModel::class.java)
        assertEquals(tMessageResponseModel,result)

    }
    @Test
    fun`should map MessageResponseModel to MessageResponseEntity`(){
        val result = MessageResponseMapper.mapToEntity(tMessageResponseModel)
        assertEquals(tMessageResponseEntity,result)
    }
}