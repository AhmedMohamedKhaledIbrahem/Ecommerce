package com.example.ecommerce.core.manager.address

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.ecommerce.core.errors.Failures
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AddressManagerImp @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : AddressManager {
    override suspend fun getAddressId(): Int {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferences.getInt(ADDRESS_KEY, -1)
            } catch (e: Exception) {
                throw Failures.CacheFailure("${e.message}")
            }
        }
    }

    override suspend fun setAddressId(addressId: Int) {
        withContext(Dispatchers.IO) {
            try {
                Log.d("TAG", "setAddressId: $addressId")
                sharedPreferences.edit { putInt(ADDRESS_KEY, addressId) }
            } catch (e: Exception) {
                throw Failures.CacheFailure("${e.message}")
            }
        }
    }

    override suspend fun clearAddressId() {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferences.edit { remove(ADDRESS_KEY) }
            } catch (e: Exception) {
                throw Failures.CacheFailure("${e.message}")
            }
        }
    }

    override suspend fun enableFetchAddress(enable: Boolean) {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferences.edit { putBoolean(FETCH_ADDRESS_KEY, enable) }
            } catch (e: Exception) {
                throw Failures.CacheFailure("${e.message}")
            }
        }
    }

    override fun getFetchAddress(): Boolean {
        return sharedPreferences.getBoolean(FETCH_ADDRESS_KEY, false)
    }

    override suspend fun clearFetchAddress() {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferences.edit { remove(FETCH_ADDRESS_KEY) }
            } catch (e: Exception) {
                throw Failures.CacheFailure("${e.message}")
            }
        }
    }


    companion object {
        private const val ADDRESS_KEY = "address_key"
        private const val FETCH_ADDRESS_KEY = "fetch_address_key"
    }
}