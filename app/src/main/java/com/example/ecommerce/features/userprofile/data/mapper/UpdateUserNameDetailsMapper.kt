package com.example.ecommerce.features.userprofile.data.mapper

import com.example.ecommerce.features.userprofile.data.models.UpdateUserNameDetailsRequestModel
import com.example.ecommerce.features.userprofile.data.models.UpdateUserNameDetailsResponseModel
import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserNameDetailsRequestEntity

object UpdateUserNameDetailsMapper {
   /* fun mapToEntity(model: UpdateUserNameDetailsResponseModel): UpdateUserNameDetailsRequestEntity {
        return UpdateUserNameDetailsRequestEntity(
            id = model.id,
            firstName = model.firstName ?: "",
            lastName = model.lastName ?: "",
            displayName = model.displayName ?: "",
        )
    }*/
    fun mapToModel(entity: UpdateUserNameDetailsRequestEntity):UpdateUserNameDetailsRequestModel{
        return UpdateUserNameDetailsRequestModel(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            displayName = entity.displayName
        )
    }
}