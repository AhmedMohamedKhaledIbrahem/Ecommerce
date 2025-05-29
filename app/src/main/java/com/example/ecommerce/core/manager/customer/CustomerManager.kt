package com.example.ecommerce.core.manager.customer

interface CustomerManager {
    fun getCustomerId():Int
   suspend fun setCustomerId(customerId:Int)
}