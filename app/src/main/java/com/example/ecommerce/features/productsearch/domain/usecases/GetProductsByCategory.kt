package com.example.ecommerce.features.productsearch.domain.usecases

import com.example.ecommerce.features.productsearch.domain.entites.ProductSearchEntity
import com.example.ecommerce.features.productsearch.domain.repositories.ProductSearchRepository
import javax.inject.Inject

class GetProductsByCategory @Inject constructor(
    private  val productSearchRepository: ProductSearchRepository
) {
    suspend operator fun invoke(categoryId:String): List<ProductSearchEntity>{
        return productSearchRepository.getProductsByCategory(categoryId)
    }
}