package com.example.ecommerce.core.service.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.ecommerce.core.database.data.dao.user.UserDao
import com.example.ecommerce.core.manager.expiry.Expiry
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.EntryPointAccessors

@HiltWorker
class TokenExpiryWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {


    val userDao: UserDao by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            TokenExpiryEntrypoint::class.java
        ).getUserDao()
    }
    val expiry: Expiry by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            TokenExpiryEntrypoint::class.java
        ).getExpiry()
    }

    override suspend fun doWork(): Result {
        val user = userDao.getUser()
        val expiryTime = user.expiredToken.toLong()
        expiry.setExpiryTime(expiryTime)

        val currentTime = System.currentTimeMillis() / 1000
        if (currentTime >= expiryTime) {
            expiry.setEnableLogout(true)
        }
        return Result.success()
    }
}