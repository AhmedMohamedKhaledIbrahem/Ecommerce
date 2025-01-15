package com.example.ecommerce.features.cart.data.data_soruce.local

import com.example.ecommerce.core.database.data.dao.cart.CartDao
import com.example.ecommerce.core.database.data.dao.cart.ItemCartDao
import com.example.ecommerce.core.database.data.entities.cart.CartWithItems
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.cart.data.mapper.CartMapper
import com.example.ecommerce.features.cart.data.mapper.ItemMapper
import com.example.ecommerce.features.cart.data.models.CartResponseModel
import javax.inject.Inject

class CartLocalDataSourceImp @Inject constructor(
    private val cartDao: CartDao,
    private val itemCartDao: ItemCartDao
) : CartLocalDataSource {
    override suspend fun insertCartWithItems(cartResponseModel: CartResponseModel) {
        try {
            val cartEntity = CartMapper.mapToEntity(cartResponseModel = cartResponseModel)
            val cartItemResponseModel = cartResponseModel.items.map {
                ItemMapper.mapToEntity(
                    cartItemResponseModel = it,
                    cartId = cartResponseModel.cartKey
                )
            }
            cartDao.insertCart(cartEntity)
            itemCartDao.insertItem(cartItemResponseModel)
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }

    override suspend fun getCart(): CartWithItems {
        return try {
            cartDao.getCartWithItems()
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }

    override suspend fun removeItem(keyItem: String) {
        try {
            itemCartDao.removeItem(keyItem = keyItem)
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }

    }

    override suspend fun updateItemsCart(cartResponseModel: CartResponseModel) {
        try {
            val cartEntity = CartMapper.mapToEntity(cartResponseModel = cartResponseModel)
            val itemsCartEntity = cartResponseModel.items.map {
                ItemMapper.mapToEntity(
                    cartItemResponseModel = it,
                    cartId = cartResponseModel.cartKey
                )
            }
//            if (cartEntity.cartId.isNotEmpty()) {
//                cartDao.updateCart(cartEntity)
//                itemCartDao.updateItem(itemsCartEntity)
//            } else {}
                cartDao.insertCart(cartEntity)
                itemCartDao.insertItem(itemsCartEntity)


        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }
}