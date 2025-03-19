package com.example.ecommerce.core.database.data.dao.address

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(customerAddressEntity: CustomerAddressEntity)

    @Update
    suspend fun updateAddress( customerAddressEntity: CustomerAddressEntity)

    @Query("select * from customerAddress ")
    suspend fun getAddress(): List<CustomerAddressEntity>



    @Query("select count(*) from customerAddress")
    suspend fun getCount(): Int

    @Query("delete from customerAddress ")
    suspend fun deleteAllAddress()



}