package com.example.ecommerce.features.product.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.product.domain.repository.ProductRepository
import com.example.ecommerce.features.product.presentation.event.ProductEvent
import com.example.ecommerce.features.product.presentation.state.ProductSearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductSearchViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {


    private val _products = MutableStateFlow<PagingData<ProductWithAllDetails>>(PagingData.empty())
    val products: StateFlow<PagingData<ProductWithAllDetails>> = _products
    private val _searchEvent: Channel<UiEvent> = Channel()
    val searchEvent = _searchEvent.receiveAsFlow()
    private val _searchState = MutableStateFlow(ProductSearchState())
    val searchState: StateFlow<ProductSearchState>
        get() = _searchState.asStateFlow()

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.SearchQuery -> {
                _searchState.update { it.copy(query = event.query) }
            }

            is ProductEvent.Searched -> {
                searchProduct()
            }

            else -> Unit
        }

    }

    private fun searchProduct() {
        viewModelScope.launch {
            val query = searchState.value.query.trim()
            productRepository.searchProduct(query)
                .cachedIn(viewModelScope)
                .onStart {
                    _searchState.update {
                        it.copy(isSearching = true)
                    }
                }
                .onEach { pagingData ->
                    _searchState.update { it.copy(pagingData, isSearching = false) }
                }
                .catch {
                    when (it) {
                        is Failures -> {
                            val mapFailureToMessage = mapFailureMessage(it)
                            _searchEvent.send(UiEvent.ShowSnackBar(mapFailureToMessage))
                        }

                        is Exception -> {
                            _searchEvent.send(UiEvent.ShowSnackBar(it.message ?: "Unknown Error"))
                        }
                    }
                }.launchIn(this)
        }
    }

}