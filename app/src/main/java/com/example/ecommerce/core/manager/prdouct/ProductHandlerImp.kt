package com.example.ecommerce.core.manager.prdouct

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import androidx.core.content.edit

class ProductHandlerImp @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ProductHandler {
    companion object {
        const val PRODUCT_UPDATE = "PRODUCT_UPDATE"
    }

    override suspend fun isProductUpdate(): Boolean {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getBoolean(PRODUCT_UPDATE, false)
        }

    }

    override suspend fun addProductUpdate(update: Boolean) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit() { putBoolean(PRODUCT_UPDATE, update) }
        }

    }

    override suspend fun deleteProductUpdate() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit() { remove(PRODUCT_UPDATE) }
        }

    }
}