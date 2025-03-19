package com.example.ecommerce.core.customer

import android.content.SharedPreferences
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
            sharedPreferences.getInt(CUSTOMER_KEY, 0)
        }
    }

    override suspend fun setCustomerId(customerId: Int) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putInt(CUSTOMER_KEY, customerId).apply()
        }

    }

}