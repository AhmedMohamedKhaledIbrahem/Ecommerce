package com.example.ecommerce.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ecommerce.core.database.data.dao.address.AddressDao
import com.example.ecommerce.core.database.data.dao.cart.CartDao
import com.example.ecommerce.core.database.data.dao.cart.ItemCartDao
import com.example.ecommerce.core.database.data.dao.category.CategoryDao
import com.example.ecommerce.core.database.data.dao.image.ImageDao
import com.example.ecommerce.core.database.data.dao.orders.OrderItemDao
import com.example.ecommerce.core.database.data.dao.orders.OrderTagDao
import com.example.ecommerce.core.database.data.dao.product.ProductCategoryCrossRefDao
import com.example.ecommerce.core.database.data.dao.product.ProductDao
import com.example.ecommerce.core.database.data.dao.user.UserDao
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.database.data.entities.cart.CartEntity
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity
import com.example.ecommerce.core.database.data.entities.category.CategoryEntity
import com.example.ecommerce.core.database.data.entities.image.ImageEntity
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.example.ecommerce.core.database.data.entities.orders.OrderTagEntity
import com.example.ecommerce.core.database.data.entities.products.ProductCategoryCrossRefEntity
import com.example.ecommerce.core.database.data.entities.products.ProductEntity
import com.example.ecommerce.core.database.data.entities.user.UserEntity

@Database(
    entities = [
        UserEntity::class,
        CustomerAddressEntity::class,
        ProductEntity::class,
        CategoryEntity::class,
        ImageEntity::class,
        ProductCategoryCrossRefEntity::class,
        CartEntity::class,
        ItemCartEntity::class,
        OrderTagEntity::class,
        OrderItemEntity::class

    ],
    version = 7,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun addressDao(): AddressDao
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun imageDao(): ImageDao
    abstract fun productCategoryCrossRefDao(): ProductCategoryCrossRefDao
    abstract fun cartDao(): CartDao
    abstract fun itemCartDao(): ItemCartDao
    abstract fun orderTagDao(): OrderTagDao
    abstract fun orderItemDao(): OrderItemDao


}