package com.example.ecommerce.core.servicelocator

import android.content.Context
import androidx.room.Room
import com.example.ecommerce.core.database.AppDatabase
import com.example.ecommerce.core.database.data.dao.orders.OrderTagDao

object DataBaseLocator {

    private var appDatabase: AppDatabase? = null


    fun provideDatabase(context: Context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(
                context.applicationContext, // Use application context to avoid memory leaks
                AppDatabase::class.java,
                "app_database"
            ).build()
        }
    }



    val orderTagDao: OrderTagDao
        get() = appDatabase?.orderTagDao()
            ?: throw IllegalStateException("Database not initialized. Call init(context) first.")

}