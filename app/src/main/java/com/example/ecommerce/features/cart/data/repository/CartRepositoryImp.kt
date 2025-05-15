package com.example.ecommerce.features.cart.data.repository

import com.example.ecommerce.core.database.data.entities.cart.CartWithItems
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.cart.data.data_soruce.local.CartLocalDataSource
import com.example.ecommerce.features.cart.data.data_soruce.remote.CartRemoteDataSource
import com.example.ecommerce.features.cart.data.mapper.AddItemRequestMapper
import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity
import com.example.ecommerce.features.cart.domain.repository.CartRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class CartRepositoryImp @Inject constructor(
    private val remoteDataSource: CartRemoteDataSource,
    private val localDataSource: CartLocalDataSource,
    private val internetConnectionChecker: InternetConnectionChecker
) : CartRepository {
    override suspend fun addItem(addItemParams: AddItemRequestEntity) {

        try {
            if (internetConnectionChecker.hasConnection()) {
                val addItemRequestModel = AddItemRequestMapper.mapToModel(addItemParams)
                val remoteAddItem =
                    remoteDataSource.addItemCart(addItemRequestModel = addItemRequestModel)
                try {
                    localDataSource.insertCartWithItems(cartResponseModel = remoteAddItem)
                } catch (failure: FailureException) {
                    throw Failures.CacheFailure("${failure.message}")
                }
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (failure: FailureException) {
            throw Failures.ServerFailure("${failure.message}")
        }

    }

    override suspend fun getCart(): CartWithItems {
        return try {
            if (internetConnectionChecker.hasConnection()) {
                localDataSource.getCart()
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (failure: FailureException) {
            throw Failures.CacheFailure("${failure.message}")
        }

    }

    override suspend fun removeItem(keyItem: String) = coroutineScope {
        if (!internetConnectionChecker.hasConnection()) {
            throw Failures.ConnectionFailure("No Internet Connection")
        }

        val local = async {
            try {
                localDataSource.removeItem(keyItem)
            } catch (e: Exception) {
                throw Failures.CacheFailure(e.message ?: "Local deletion failed")
            }
        }
        val remote = async {
            try {
                remoteDataSource.removeItem(itemHash = keyItem)
            } catch (e: Exception) {
                throw Failures.ServerFailure(e.message ?: "Remote deletion failed")
            }
        }
        local.await()
        remote.await()

    }

    override suspend fun updateQuantity(itemId: Int, newQuantity: Int) {
        try {
            if (internetConnectionChecker.hasConnection()) {
                localDataSource.updateQuantity(itemId = itemId, newQuantity = newQuantity)
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (failure: FailureException) {
            throw Failures.CacheFailure("${failure.message}")
        }
    }

    override suspend fun updateItemsCart() {
        try {
            if (internetConnectionChecker.hasConnection()) {
                val getItemsCart = remoteDataSource.getCart()
                try {
                    localDataSource.insertCartWithItems(cartResponseModel = getItemsCart)
                } catch (failure: FailureException) {
                    throw Failures.CacheFailure("${failure.message}")
                }
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (failure: FailureException) {
            throw Failures.ServerFailure("${failure.message}")
        }
    }

    override suspend fun clearCart() {
        try {
            if (internetConnectionChecker.hasConnection()) {
                remoteDataSource.clearCart()
                try {
                    localDataSource.clearCart()
                } catch (failure: FailureException) {
                    throw Failures.CacheFailure("${failure.message}")
                }
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (failure: FailureException) {
            throw Failures.ServerFailure("${failure.message}")
        }
    }
}