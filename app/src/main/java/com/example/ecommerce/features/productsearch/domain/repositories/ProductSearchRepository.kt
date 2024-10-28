package com.example.ecommerce.features.productsearch.domain.repositories

import com.example.ecommerce.features.productsearch.domain.entites.ProductSearchEntity

interface ProductSearchRepository {
 suspend fun getProductsByCategory(categoryId:String):List<ProductSearchEntity>
}