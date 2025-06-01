package com.example.ecommerce.features.notification.data.mapper

import com.example.ecommerce.features.notification.data.model.NotificationRequestModel
import com.example.ecommerce.features.notification.data.model.NotificationResponseModel
import com.example.ecommerce.features.notification.domain.entity.NotificationRequestEntity
import com.example.ecommerce.features.notification.domain.entity.NotificationResponseEntity

fun NotificationRequestEntity.toModel(): NotificationRequestModel {
    return NotificationRequestModel(
        this.token
    )
}

fun NotificationResponseModel.toEntity(): NotificationResponseEntity {
    return NotificationResponseEntity(
        this.message
    )

}