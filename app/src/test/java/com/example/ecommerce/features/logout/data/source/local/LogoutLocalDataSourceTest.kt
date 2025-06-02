package com.example.ecommerce.features.logout.data.source.local

import com.example.ecommerce.core.database.data.dao.logout.LogoutDao
import com.example.ecommerce.core.manager.address.AddressManager
import com.example.ecommerce.core.manager.customer.CustomerManager
import com.example.ecommerce.core.manager.prdouct.ProductHandler
import com.example.ecommerce.core.manager.token.TokenManager
import com.example.ecommerce.features.failureException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class LogoutLocalDataSourceTest {
    private val dao = mockk<LogoutDao>(relaxed = true)
    private val tokenManager = mockk<TokenManager>(relaxed = true)
    private val productHandler = mockk<ProductHandler>(relaxed = true)
    private val customerManager = mockk<CustomerManager>(relaxed = true)
    private val addressManager = mockk<AddressManager>(relaxed = true)
    private lateinit var localDataSource: LogoutLocalDataSource

    @Before
    fun setup() {
        localDataSource = LogoutLocalDataSourceImp(
            dao = dao,
            tokenManager = tokenManager,
            productHandler = productHandler,
            customerManager = customerManager,
            addressManager = addressManager
        )
    }

    @Test
    fun `logout should delete all data from database , clear jwt token, clear customer id and address id`() =
        runTest {
            localDataSource.logout()
            coVerify(exactly = 1) { dao.deleteImage() }
            coVerify(exactly = 1) { dao.deleteCategory() }
            coVerify(exactly = 1) { dao.deleteCustomerAddress() }
            coVerify(exactly = 1) { dao.deleteOrders() }
            coVerify(exactly = 1) { dao.deleteProduct() }
            coVerify(exactly = 1) { dao.deleteCart() }
            coVerify(exactly = 1) { dao.deleteItemCart() }
            coVerify(exactly = 1) { dao.deleteOrderTage() }
            coVerify(exactly = 1) { dao.deleteProductCategory() }
            coVerify(exactly = 1) { dao.deleteProductCategoryCross() }
            coVerify(exactly = 1) { dao.deleteUser() }
            coVerify(exactly = 1) { tokenManager.clearToken() }
            coVerify(exactly = 1) { productHandler.deleteProductUpdate() }
            coVerify(exactly = 1) { customerManager.clearCustomerId() }
            coVerify(exactly = 1) { addressManager.clearAddressId() }
        }

    @Test
    fun `logout should throw FailureException on error`() = runTest {
        coEvery { dao.deleteImage() } throws RuntimeException(RUN_TIME_ERROR)
        val exception = failureException {
            localDataSource.logout()
        }
        assertEquals(RUN_TIME_ERROR, exception.message)
        coVerify(exactly = 1) { dao.deleteImage() }
        coVerify(exactly = 0) { dao.deleteCategory() }
        coVerify(exactly = 0) { dao.deleteCustomerAddress() }
        coVerify(exactly = 0) { dao.deleteOrders() }
        coVerify(exactly = 0) { dao.deleteProduct() }
        coVerify(exactly = 0) { dao.deleteCart() }
        coVerify(exactly = 0) { dao.deleteItemCart() }
        coVerify(exactly = 0) { dao.deleteOrderTage() }
        coVerify(exactly = 0) { dao.deleteProductCategory() }
        coVerify(exactly = 0) { dao.deleteProductCategoryCross() }
        coVerify(exactly = 0) { dao.deleteUser() }
        coVerify(exactly = 0) { tokenManager.clearToken() }
        coVerify(exactly = 0) { productHandler.deleteProductUpdate() }
        coVerify(exactly = 0) { customerManager.clearCustomerId() }
        coVerify(exactly = 0) { addressManager.clearAddressId() }
    }


    companion object {
        private const val RUN_TIME_ERROR = "database error"
    }


}