package com.example.ecommerce.features.productsearch.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.features.productsearch.domain.entites.ProductSearchEntity
import com.example.ecommerce.features.productsearch.domain.usecases.GetProductsByCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductSearchByCategoryViewModel @Inject constructor(
    private val getProductsByCategory: GetProductsByCategory
) : ViewModel() {
    private val _products = MutableLiveData<List<ProductSearchEntity>>()
    val products: LiveData<List<ProductSearchEntity>> get() = _products

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchProductsByCategory(categoryId: String) {

        viewModelScope.launch {
            try {
                val products: List<ProductSearchEntity> =
                    getProductsByCategory(categoryId = categoryId)
                _products.value = products

            } catch (failure: Failures) {
                _error.value = mapFailureMessage(failure)
            } catch (e: Exception) {
                _error.value = "Unexpected Error: ${e.message}"
            }
        }
    }

}