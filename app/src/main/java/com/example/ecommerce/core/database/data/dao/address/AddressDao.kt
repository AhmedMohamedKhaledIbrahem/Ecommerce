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
    suspend fun updateAddress(customerAddressEntity: CustomerAddressEntity)

    @Delete
    suspend fun deleteAddress(customerAddressEntity: CustomerAddressEntity)

    @Query("select * from customerAddress ")
    suspend fun getAddress(): List<CustomerAddressEntity>

    @Query("select count(*) from customerAddress")
    suspend fun getCount(): Int

    @Query("delete from customerAddress ")
    suspend fun deleteAllAddress()

    @Query("select * from customerAddress where id=:customerAddressId and isSelect = 1")
    suspend fun getSelectAddress(customerAddressId: Int): CustomerAddressEntity


    @Query("update customerAddress set isSelect = 0 where id != :customerAddressId")
    suspend fun unSelectAddress(customerAddressId: Int)

    @Query("update customerAddress set isSelect = 1 where id = :customerAddressId")
    suspend fun selectAddress(customerAddressId: Int)

    @Query("select Count(*) from customerAddress where email = :email ")
    suspend fun isEmailExist(email: String): Int

    @Query("select id from customerAddress where email = :email")
    suspend fun getCustomerId(email: String): Int

}