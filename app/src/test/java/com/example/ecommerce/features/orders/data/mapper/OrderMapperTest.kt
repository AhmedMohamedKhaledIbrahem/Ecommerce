package com.example.ecommerce.features.orders.data.mapper

import com.example.ecommerce.features.orders.tCreateOrderRequestEntity
import com.example.ecommerce.features.orders.tCreateOrderRequestModelJson
import com.example.ecommerce.features.orders.tCreateOrderResponseEntity
import com.example.ecommerce.features.orders.tCreateOrderResponseModelJson
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderMapperTest {
    @Test
    fun `should map OrderRequestEntity to OrderRequestModel`() {
        val orderRequestModel = OrderMapper.mapEntityToModel(tCreateOrderRequestEntity)
        assertEquals(tCreateOrderRequestModelJson, orderRequestModel)
    }

    @Test
    fun `should map OrderResponseModel to OrderResponseEntity`() {
        val orderRequestEntity = OrderMapper.mapModelToEntity(tCreateOrderResponseModelJson)
        assertEquals(tCreateOrderResponseEntity, orderRequestEntity)
    }
}