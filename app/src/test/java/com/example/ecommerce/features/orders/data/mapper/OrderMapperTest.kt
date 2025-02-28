package com.example.ecommerce.features.orders.data.mapper

import com.example.ecommerce.features.orders.tCreateOrderRequestEntity
import com.example.ecommerce.features.orders.tCreateOrderRequestModelJson
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderMapperTest {
    @Test
    fun `should map OrderRequestEntity to OrderRequestModel`() {
        val orderRequestModel = OrderMapper.mapEntityToModel(tCreateOrderRequestEntity)
        assertEquals(tCreateOrderRequestModelJson, orderRequestModel)
    }


}