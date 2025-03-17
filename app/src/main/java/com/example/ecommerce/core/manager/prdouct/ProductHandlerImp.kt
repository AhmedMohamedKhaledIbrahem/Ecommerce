package com.example.ecommerce.core.manager.prdouct

import android.content.SharedPreferences
import javax.inject.Inject

class ProductHandlerImp @Inject constructor(
    private val sharedPreferences: SharedPreferences
):ProductHandler{
    companion object{
        const val PRODUCT_UPDATE="PRODUCT_UPDATE"
    }
    override suspend fun isProductUpdate(): Boolean {
        return sharedPreferences.getBoolean(PRODUCT_UPDATE, false)
    }

    override suspend fun addProductUpdate(update: Boolean) {
       sharedPreferences.edit().putBoolean(PRODUCT_UPDATE,update).apply()
    }

    override suspend fun deleteProductUpdate() {
       sharedPreferences.edit().remove(PRODUCT_UPDATE).apply()
    }
}