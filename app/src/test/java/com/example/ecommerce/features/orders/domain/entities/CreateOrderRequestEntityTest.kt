package com.example.ecommerce.features.orders.domain.entities

import com.example.ecommerce.features.orders.data.mapper.OrderMapper
import com.example.ecommerce.features.orders.tCreateOrderRequestEntity

import com.example.ecommerce.features.orders.tCreateOrderRequestModelJson
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class CreateOrderRequestEntityTest {
    @Test
    fun `should map convert create order request entity to create order request model`() = runTest {
        val createOrderRequestModel = OrderMapper.mapEntityToModel(
            entity = tCreateOrderRequestEntity
        )
        assertEquals(tCreateOrderRequestModelJson, createOrderRequestModel)

    }
}