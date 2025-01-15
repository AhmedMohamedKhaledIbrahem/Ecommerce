package com.example.ecommerce.features.product.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails
import com.example.ecommerce.features.product.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class ProductSearchViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val searchQuery = _searchQuery.asStateFlow()


    @OptIn(ExperimentalCoroutinesApi::class)
    val product: Flow<PagingData<ProductWithAllDetails>> = searchQuery
        .flatMapLatest { query ->
            productRepository.searchProduct(query) // No need for distinctUntilChanged here
        }.cachedIn(viewModelScope)


    fun updateSearchQuery(query: String) {
        _searchQuery.value = query

    }

}