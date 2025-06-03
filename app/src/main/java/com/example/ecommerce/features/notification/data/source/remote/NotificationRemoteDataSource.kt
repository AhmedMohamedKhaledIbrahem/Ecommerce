package com.example.ecommerce.features.notification.data.source.remote

import com.example.ecommerce.features.notification.data.model.NotificationRequestModel
import com.example.ecommerce.features.notification.data.model.NotificationResponseModel

interface NotificationRemoteDataSource {
  suspend  fun saveToken(notificationRequestParams: NotificationRequestModel): NotificationResponseModel


}