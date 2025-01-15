package com.example.ecommerce.features.cart.data.models

import com.example.ecommerce.features.cart.dummyCartResponseModel
import org.junit.Test
import kotlin.test.assertEquals

class CartResponseModelTest {

    private val cartKey = "1"
    private val quantityResponseModel = QuantityResponseModel(value = 1)
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
    fun `cartResponseModel should have correct properties`() {
        val cartResponseModel = CartResponseModel(
            cartKey = cartKey,
            items = items
        )

        assertEquals(dummyCartResponseModel,cartResponseModel)
    }
}