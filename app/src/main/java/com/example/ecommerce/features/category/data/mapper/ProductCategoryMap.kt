package com.example.ecommerce.features.category.data.mapper

import com.example.ecommerce.core.database.data.entities.category.CategoryEntity
import com.example.ecommerce.features.category.data.model.CategoryResponseModel
import com.example.ecommerce.features.category.data.model.DataCategoryResponseModel

fun CategoryResponseModel.toDomainList(): List<CategoryEntity> {
    return data.map { it.toDomain() }
}

fun DataCategoryResponseModel.toDomain(): CategoryEntity {
    return CategoryEntity(
        id = this.id,
        name = this.name
    )
}


