package com.example.ecommerce.features.address.data.mapper

import com.example.ecommerce.features.address.data.models.ShippingInfoRequestModel
import com.example.ecommerce.features.address.data.models.ShippingInfoResponseModel
import com.example.ecommerce.features.address.domain.entites.ShippingInfoRequestEntity
import com.example.ecommerce.features.address.domain.entites.ShippingInfoResponseEntity
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

//class ShippingInfoMapperTest {
//    private val tShippingInfoResponseEntity = ShippingInfoResponseEntity(
//        firstName = "John",
//        lastName = "Doe",
//        address = "123 Main St",
//        city = "Springfield",
//        state = "IL",
//        postCode = "62701",
//        country = "US",
//
//        )
//    private val tShippingInfoRequestModel = ShippingInfoRequestModel(
//        firstName = "John",
//        lastName = "Doe",
//        address = "123 Main St",
//        city = "Springfield",
//        state = "IL",
//        postCode = "62701",
//        country = "US",
//
//        )
//    private val tShippingInfoRequestEntity = ShippingInfoRequestEntity(
//        firstName = "John",
//        lastName = "Doe",
//        address = "123 Main St",
//        city = "Springfield",
//        state = "IL",
//        postCode = "62701",
//        country = "US",
//        )
//
//    @Test
//    fun `should map ShippingInfoResponseModel to the ShippingInfoResponseEntity`() = runTest {
//        val json = fixture("shippingInfo.json")
//        val shippingInfoModelResponseJson =
//            Gson().fromJson(json, ShippingInfoResponseModel::class.java)
//        val result = ShippingInfoMapper.mapToEntity(model = shippingInfoModelResponseJson)
//        assertEquals(tShippingInfoResponseEntity, result)
//    }
//
//    @Test
//    fun `should map ShippingInfoRequestEntity to the ShippingInfoRequestModel`() = runTest {
//        val result = ShippingInfoMapper.mapToModel(entity = tShippingInfoRequestEntity)
//        assertEquals(tShippingInfoRequestModel, result)
//    }
//}