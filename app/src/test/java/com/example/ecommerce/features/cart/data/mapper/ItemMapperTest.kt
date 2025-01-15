package com.example.ecommerce.features.cart.data.mapper

import com.example.ecommerce.features.cart.data.models.CartItemResponseModel
import com.example.ecommerce.features.cart.data.models.QuantityResponseModel
import com.example.ecommerce.features.cart.dummyItemEntity
import org.junit.Test
import kotlin.test.assertEquals

class ItemMapperTest {
    private val quantityResponseModel = QuantityResponseModel(value = 1)
    private val cartKey = "1"
    private val items = listOf(
        CartItemResponseModel(
            itemKey = "1te2st",
            id = 1,
            name = "test",
            price = "1000",
            quantity = quantityResponseModel,
            imageItemLink = "test"
        )
    )

    @Test
    fun `should map CartResponseModel to CartEntity `() {

        val itemCartEntity = items.map {
            ItemMapper.mapToEntity(
                cartItemResponseModel = it,
                cartId = "1"
            )
        }

        assertEquals(dummyItemEntity, itemCartEntity)


    }
}