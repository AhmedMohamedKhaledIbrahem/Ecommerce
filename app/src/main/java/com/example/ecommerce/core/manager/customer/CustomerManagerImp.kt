package com.example.ecommerce.core.manager.customer

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.ecommerce.core.errors.Failures
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CustomerManagerImp @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : CustomerManager {
    companion object {
        private const val CUSTOMER_KEY = "customer_key"
    }

    override suspend fun getCustomerId(): Int {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferences.getInt(CUSTOMER_KEY, 0)
            } catch (e: Exception) {
                throw Failures.CacheFailure("${e.message}")
            }
        }
    }

    override suspend fun setCustomerId(customerId: Int) {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferences.edit{ putInt(CUSTOMER_KEY, customerId) }
            } catch (e: Exception) {
                throw Failures.CacheFailure("${e.message}")
            }

        }

    }

}