package com.example.ecommerce.features.userprofile.data.mapper

import com.example.ecommerce.features.userprofile.data.models.GetImageProfileResponseModel
import com.example.ecommerce.features.userprofile.domain.entites.GetImageProfileResponseEntity
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import org.junit.Test
import kotlin.test.assertEquals

class GetImageProfileMapperTest {

    private val tGetImageProfileResponseModel = GetImageProfileResponseModel(
        userId = 1,
        profileImage = ""
    )
    private val tGetImageProfileResponseEntity = GetImageProfileResponseEntity(
        userId = 1,
        profileImage = ""
    )
    private val tUserId = 1

    @Test
    fun `load getImageProfile and parse as AuthenticationResponseModel object`() {
        val json = fixture("getImageProfile.json")
        val result: GetImageProfileResponseModel = Gson().fromJson(
            json,
            GetImageProfileResponseModel::class.java
        )
        assertEquals(tGetImageProfileResponseModel,result)
    }
    @Test
     fun `should map GetImageProfileResponseModel to the GetImageProfileResponseEntity`(){
         val mapToEntity = GetImageProfileMapper.mapToEntity(model = tGetImageProfileResponseModel)
        assertEquals(tGetImageProfileResponseEntity,mapToEntity)
     }
}