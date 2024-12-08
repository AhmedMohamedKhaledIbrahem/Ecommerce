package com.example.ecommerce.features.authentication.data.mapper

import com.example.ecommerce.features.authentication.data.models.CheckVerificationRequestModel
import com.example.ecommerce.features.authentication.domain.entites.CheckVerificationRequestEntity

object CheckVerificationRequestMapper {
    fun mapToModel(entity: CheckVerificationRequestEntity): CheckVerificationRequestModel {
        return CheckVerificationRequestModel(
            email = entity.email,
            code = entity.code
        )
    }
}