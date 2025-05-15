package com.example.ecommerce.core.manager.customer

interface CustomerManager {
   suspend fun getCustomerId():Int
   suspend fun setCustomerId(customerId:Int)
}