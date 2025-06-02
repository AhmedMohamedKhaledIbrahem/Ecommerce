package com.example.ecommerce.core.service.work

import com.example.ecommerce.core.database.data.dao.user.UserDao
import com.example.ecommerce.core.manager.expiry.Expiry
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface TokenExpiryEntrypoint {
    fun getUserDao(): UserDao
    fun getExpiry(): Expiry
}