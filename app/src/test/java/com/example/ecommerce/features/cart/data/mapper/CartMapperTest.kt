package com.example.ecommerce.features.cart.data.mapper

import com.example.ecommerce.features.cart.data.models.CartItemResponseModel
import com.example.ecommerce.features.cart.data.models.CartResponseModel
import com.example.ecommerce.features.cart.data.models.QuantityResponseModel
import com.example.ecommerce.features.cart.dummyCartEntity
import org.junit.Test
import kotlin.test.assertEquals

class CartMapperTest {

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
    fun `should map CartResponseModel to CartEntity `() {
        val cartResponseModel = CartResponseModel(
            cartKey = cartKey,
            items = items,
            cartHash = cartHash
        )
        val cartEntity = CartMapper.mapToEntity(cartResponseModel = cartResponseModel)
        assertEquals(dummyCartEntity, cartEntity)

    }
}