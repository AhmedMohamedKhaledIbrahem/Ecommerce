package com.example.ecommerce.core.customer

import android.content.SharedPreferences
import javax.inject.Inject

class CustomerManagerImp @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : CustomerManager {
    companion object {
        private const val CUSTOMER_KEY = "customer_key"
    }

    override fun getCustomerId(): Int {
        return sharedPreferences.getInt(CUSTOMER_KEY, 0)
    }

    override fun setCustomerId(customerId: Int) {
        sharedPreferences.edit().putInt(CUSTOMER_KEY, customerId).apply()
    }

}