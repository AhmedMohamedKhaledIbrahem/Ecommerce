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
    private val cartHash = "d24ac79a778cebf33b54bd7869146494"

    @Test
    fun `cartResponseModel should have correct properties`() {
        val cartResponseModel = CartResponseModel(
            cartKey = cartKey,
            items = items,
            cartHash = cartHash
        )

        assertEquals(dummyCartResponseModel,cartResponseModel)
    }
}