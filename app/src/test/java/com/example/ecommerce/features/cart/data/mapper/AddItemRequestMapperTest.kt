package com.example.ecommerce.features.cart.data.mapper

import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity
import com.example.ecommerce.features.cart.dummyAddItemRequestModel
import org.junit.Test
import kotlin.test.assertEquals

class AddItemRequestMapperTest {
    private val addItemRequestEntity = AddItemRequestEntity(
        id = 1,
        quantity = 1
    )

    @Test
    fun `should map AddItemRequestEntity to AddItemRequestModel`() {

        val addItemRequestModel = AddItemRequestMapper.mapToModel(addItemRequestEntity)
        assertEquals(dummyAddItemRequestModel, addItemRequestModel)
    }
}