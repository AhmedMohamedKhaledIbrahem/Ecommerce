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

    override suspend fun updateQuantity(itemId: Int, newQuantity: Int) {
        try {
            itemCartDao.updateQuantity(itemId = itemId, newQuantity = newQuantity)
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }

    override suspend fun getCartCount(): Int {
        return try {
            cartDao.getCartCount()
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }

    override suspend fun clearCart() {
        try {
            cartDao.deleteCart()
        } catch (e: Exception) {
            throw FailureException("${e.message}")

        }
    }
}