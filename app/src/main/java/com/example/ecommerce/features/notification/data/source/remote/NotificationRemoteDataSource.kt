package com.example.ecommerce.features.notification.data.source.remote

interface NotificationRemoteDataSource {
  suspend  fun saveToken(token: String)


}