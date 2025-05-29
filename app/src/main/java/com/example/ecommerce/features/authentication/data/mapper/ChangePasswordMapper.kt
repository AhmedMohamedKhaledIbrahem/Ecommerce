package com.example.ecommerce.features.authentication.data.mapper

import com.example.ecommerce.features.authentication.data.models.ChangePasswordRequestModel
import com.example.ecommerce.features.authentication.domain.entites.ChangePasswordRequestEntity

fun ChangePasswordRequestEntity.toModel(): ChangePasswordRequestModel {
    return ChangePasswordRequestModel(
        userId = this.userId,
        password = this.password
    )
}