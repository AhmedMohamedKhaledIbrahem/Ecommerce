package com.example.ecommerce.core.customer

interface CustomerManager {
   suspend fun getCustomerId():Int
   suspend fun setCustomerId(customerId:Int)
}