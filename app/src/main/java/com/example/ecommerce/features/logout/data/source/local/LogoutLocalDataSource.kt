package com.example.ecommerce.features.logout.data.source.local

import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.database.data.dao.logout.LogoutDao
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.manager.address.AddressManager
import com.example.ecommerce.core.manager.customer.CustomerManager
import com.example.ecommerce.core.manager.prdouct.ProductHandler
import com.example.ecommerce.core.manager.token.TokenManager
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
            dao.deleteImage()
            dao.deleteCategory()
            dao.deleteCustomerAddress()
            dao.deleteOrders()
            dao.deleteProduct()
            dao.deleteCart()
            dao.deleteItemCart()
            dao.deleteOrderTage()
            dao.deleteProductCategory()
            dao.deleteProductCategoryCross()
            dao.deleteUser()
            tokenManager.clearToken()
            tokenManager.clearVerificationStatus()
            productHandler.deleteProductUpdate()
            customerManager.clearCustomerId()
            addressManager.clearAddressId()


        } catch (e: Exception) {
            throw FailureException(e.message ?: Unknown_Error)
        }

    }

}