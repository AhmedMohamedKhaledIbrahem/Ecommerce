package com.example.ecommerce.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ecommerce.core.data.entities.CustomerAddressEntity

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(customerAddressEntity: CustomerAddressEntity)

    @Update
    suspend fun updateAddress(customerAddressEntity: CustomerAddressEntity)

    @Query("select * from customerAddress where userId =:userId Limit 1")
    suspend fun getAddressById(userId: Int): CustomerAddressEntity?

    @Query("select * from customerAddress where userId =:id Limit 1 ")
    suspend fun getAddress(id:Int): CustomerAddressEntity

    @Query("Delete from customerAddress")
    suspend fun deleteAddress()


}