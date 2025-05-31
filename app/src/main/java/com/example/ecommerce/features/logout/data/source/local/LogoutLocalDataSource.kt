package com.example.ecommerce.features.logout.data.source.local

import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.database.data.dao.logout.LogoutDao
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.manager.address.AddressManager
import com.example.ecommerce.core.manager.customer.CustomerManager
import com.example.ecommerce.core.manager.fcm.FcmDeviceToken
import com.example.ecommerce.core.manager.prdouct.ProductHandler
import com.example.ecommerce.core.manager.token.TokenManager
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

interface LogoutLocalDataSource {
    suspend fun logout()
}

class LogoutLocalDataSourceImp @Inject constructor(
    private val dao: LogoutDao,
    private val tokenManager: TokenManager,
    private val productHandler: ProductHandler,
    private val customerManager: CustomerManager,
    private val addressManager: AddressManager,

) : LogoutLocalDataSource {
    override suspend fun logout() = coroutineScope {
        try {
            awaitAll(
                async { dao.deleteImage() },
                async { dao.deleteCategory() },
                async { dao.deleteCustomerAddress() },
                async { dao.deleteOrders() },
                async { dao.deleteProduct() },
                async { dao.deleteCart() },
                async { dao.deleteItemCart() },
                async { dao.deleteOrderTage() },
                async { dao.deleteProductCategory() },
                async { dao.deleteProductCategoryCross() },
                async { dao.deleteUser() },
                async { tokenManager.clearToken() },
                async { productHandler.deleteProductUpdate() },
                async { customerManager.clearCustomerId() },
                async { addressManager.clearAddressId() },

            )
            Unit

        } catch (e: Exception) {
            throw FailureException(e.message ?: Unknown_Error)
        }

    }

}