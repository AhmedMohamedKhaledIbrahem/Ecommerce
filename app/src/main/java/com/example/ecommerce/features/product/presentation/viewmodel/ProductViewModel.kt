package com.example.ecommerce.features.product.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails

import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.product.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _productState = MutableSharedFlow<UiState<Any>>(replay = 1, extraBufferCapacity = 1)
    val productState: SharedFlow<UiState<Any>> get() = _productState.asSharedFlow()
    val productsPaged: Flow<PagingData<ProductWithAllDetails>> = productRepository.getProducts()
        .cachedIn(viewModelScope)


    fun fetchProductsFromRemote(page: Int, perPage: Int) {
        productUiState(
            operation = { productRepository.fetchProduct(page, perPage) },
            onSuccess = { result ->
                _productState.emit(UiState.Success(result, "fetchProducts"))
            },
            source = "fetchProducts"
        )

    }


    private fun <T> productUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    ) {
        viewModelScope.launch {
            _productState.emit(UiState.Loading(source))
            try {
                val result = operation()
                onSuccess(result)
            } catch (e: Exception) {
                _productState.emit(UiState.Error(e.message ?: "Unknown Error", source))
            } catch (failure: Failures) {
                _productState.emit(UiState.Error(mapFailureMessage(failure), source))
            }
        }

    }
}