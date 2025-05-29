package com.example.ecommerce.features.authentication.data.mapper

import com.example.ecommerce.features.authentication.data.models.ConfirmPasswordResetRequestModel
import com.example.ecommerce.features.authentication.domain.entites.ConfirmPasswordResetRequestEntity

fun ConfirmPasswordResetRequestEntity.toModel(): ConfirmPasswordResetRequestModel {
    return ConfirmPasswordResetRequestModel(
        userId = this.userId,
        otp = this.otp,
        password = this.password
    )
}