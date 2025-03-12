package com.example.ecommerce.features.orders.data.mapper

import com.example.ecommerce.features.orders.data.models.ImageResponseModel
import com.example.ecommerce.features.orders.domain.entities.ImageResponseEntity

object ImageItemMapper {
    fun mapModelToEntity(model: ImageResponseModel): ImageResponseEntity {
        return ImageResponseEntity(
            imagePath = model.imagePath
        )
    }
    fun mapEntityToModel(entity: ImageResponseEntity): ImageResponseModel {
        return ImageResponseModel(
            imagePath = entity.imagePath
        )
    }
}