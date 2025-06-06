package com.example.ecommerce.features.address.data.mapper

import com.example.ecommerce.features.address.data.models.BillingInfoRequestModel
import com.example.ecommerce.features.address.data.models.BillingInfoResponseModel
import com.example.ecommerce.features.address.domain.entites.BillingInfoRequestEntity
import com.example.ecommerce.features.address.domain.entites.BillingInfoResponseEntity

object BillingInfoMapper {
    fun mapToEntity(model: BillingInfoResponseModel): BillingInfoResponseEntity {
        return BillingInfoResponseEntity(
            firstName = model.firstName,
            lastName = model.lastName,
            email = model.email,
            address = model.address,
            city = model.city,
            postCode = model.postCode,
            country = model.country,
            phone = model.phone,
        )
    }

    fun mapToModel(entity: BillingInfoRequestEntity): BillingInfoRequestModel {
        return BillingInfoRequestModel(
            firstName = entity.firstName,
            lastName = entity.lastName,
            address = entity.address,
            city = entity.city,
            postCode = entity.postCode,
            country = entity.country,
            email = entity.email,
            phone = entity.phone,
        )
    }

    fun mapBillingInfoResponseModelToBillingInfoRequestModel(model: BillingInfoResponseModel): BillingInfoRequestModel {
        return BillingInfoRequestModel(
            firstName = model.firstName,
            lastName = model.lastName,
            address = model.address,
            city = model.city,
            postCode = model.postCode,
            country = model.country,
            email = model.email,
            phone = model.phone,

            )
    }
}