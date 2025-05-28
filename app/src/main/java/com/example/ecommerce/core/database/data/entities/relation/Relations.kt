package com.example.ecommerce.core.database.data.entities.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.ecommerce.core.database.data.entities.category.ProductCategoryEntity
import com.example.ecommerce.core.database.data.entities.image.ImageEntity
import com.example.ecommerce.core.database.data.entities.products.ProductCategoryCrossRefEntity
import com.example.ecommerce.core.database.data.entities.products.ProductEntity

data class ProductWithAllDetails(
    @Embedded val product: ProductEntity,

    @Relation(
        parentColumn = "productIdJson",
        entityColumn = "productId"
    )
    val images: List<ImageEntity>,

    @Relation(
        parentColumn = "productIdJson",
        entityColumn = "categoryIdJson",
        associateBy = Junction(ProductCategoryCrossRefEntity::class)
    )
    val categories: List<ProductCategoryEntity>,
    )


