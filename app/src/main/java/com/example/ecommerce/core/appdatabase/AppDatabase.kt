package com.example.ecommerce.core.appdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ecommerce.core.data.dao.AddressDao
import com.example.ecommerce.core.data.dao.UserDao
import com.example.ecommerce.core.data.entities.CustomerAddressEntity
import com.example.ecommerce.core.data.entities.UserEntity

@Database(entities = [UserEntity::class , CustomerAddressEntity::class], version = 2, exportSchema = false,)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun addressDao(): AddressDao
}