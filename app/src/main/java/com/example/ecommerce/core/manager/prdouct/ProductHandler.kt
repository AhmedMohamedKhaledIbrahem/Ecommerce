package com.example.ecommerce.core.manager.prdouct

interface ProductHandler {
    suspend fun isProductUpdate():Boolean
    suspend fun addProductUpdate(update:Boolean)
    suspend fun deleteProductUpdate()
}