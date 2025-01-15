package com.example.ecommerce.features.product.data.mapper

import com.example.ecommerce.core.database.data.entities.products.ProductEntity
import com.example.ecommerce.features.product.data.model.ProductResponseModel

object ProductMapper {
    fun toEntity(model: ProductResponseModel): ProductEntity {
        return ProductEntity(
            productIdJson = model.id,
            name = model.name,
            description = model.description,
            shortDescription = model.shortDescription,
            price = (model.price.price.toInt() / 100).toString(),
            reviewCount = model.reviewCount,
            ratingCount = model.ratingCount,
            isStock = model.stock.isStock,
            statusStock = model.stock.statusStock,
            // currency = model.currency.currencyCode?:"EG",
        )
    }
}