package com.example.ecommerce.core.manager.address

import android.content.SharedPreferences
import android.util.Log
import com.example.ecommerce.core.errors.Failures
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.content.edit


class AddressManagerImp @Inject constructor(
    private val sharedPreferences: SharedPreferences
): AddressManager{
    override suspend fun getAddressId(): Int {
        return withContext(Dispatchers.IO){
            try {
                sharedPreferences.getInt(ADDRESS_KEY, -1)
            }catch (e: Exception){
                throw Failures.CacheFailure("${e.message}")
            }
        }
    }

    override suspend fun setAddressId(addressId: Int) {
        withContext(Dispatchers.IO){
            try {
                Log.d("TAG", "setAddressId: $addressId")
                sharedPreferences.edit{ putInt(ADDRESS_KEY, addressId) }
            }catch (e: Exception){
                throw Failures.CacheFailure("${e.message}")
            }
        }
    }


    companion object{
        private const val ADDRESS_KEY = "address_key"
    }
}