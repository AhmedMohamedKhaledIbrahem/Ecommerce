package com.example.ecommerce.features.cart

import com.example.ecommerce.core.database.data.entities.cart.CartWithItems
import com.example.ecommerce.features.cart.data.mapper.AddItemRequestMapper
import com.example.ecommerce.features.cart.data.mapper.CartMapper
import com.example.ecommerce.features.cart.data.mapper.ItemMapper
import com.example.ecommerce.features.cart.data.models.CartResponseModel
import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson

 val dummyCartResponseModel: CartResponseModel = fixture("cartWithItems.json").run {
    Gson().fromJson(this, CartResponseModel::class.java)
}
val dummyCartEntity = CartMapper.mapToEntity(cartResponseModel =dummyCartResponseModel)

val dummyItemEntity = dummyCartResponseModel.items.map {
    ItemMapper.mapToEntity(
        cartItemResponseModel = it,
        cartId = dummyCartEntity.cartId
    )
}
val dummyCartWithItemEntity = CartWithItems(
    cart = dummyCartEntity,
    items = dummyItemEntity
)


val dummyAddItemRequestEntity = AddItemRequestEntity(
    id = "1",
    quantity = "1"
)
val dummyAddItemRequestModel = AddItemRequestMapper.mapToModel(dummyAddItemRequestEntity)

const val keyItem = "1te2st"


