package com.example.ecommerce.features.product.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.Page
import com.example.ecommerce.core.constants.PerPage
import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.product.domain.repository.ProductRepository
import com.example.ecommerce.features.product.presentation.event.ProductEvent
import com.example.ecommerce.features.product.presentation.state.ProductDetailsState
import com.example.ecommerce.features.product.presentation.state.ProductPagedState
import com.example.ecommerce.features.product.presentation.state.ProductRemoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private var pagingJob: Job? = null

    private val _productPagedEvent: Channel<UiEvent> = Channel()
    val productPagedEvent = _productPagedEvent.receiveAsFlow()

    private val _productPagedState = MutableStateFlow(ProductPagedState())
    val productPagedState: StateFlow<ProductPagedState>
        get() = _productPagedState.asStateFlow()

    private val _productRemoteState = MutableStateFlow(ProductRemoteState())
    val productRemoteState: StateFlow<ProductRemoteState>
        get() = _productRemoteState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProductRemoteState()
        )

    private val _productDetailsState = MutableStateFlow(ProductDetailsState())
    val productDetailsState: StateFlow<ProductDetailsState>
        get() = _productDetailsState.asStateFlow()

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.FetchProductRemote -> {
                fetchProductRemote()
            }

            is ProductEvent.FetchProductPaging -> {
                fetchProductPaging()
            }

            is ProductEvent.Input.InputProductDetails -> {
                _productDetailsState.update { it.copy(product = event.product) }
            }

            is ProductEvent.Input.FilterByCategory -> {
                _productPagedState.update { it.copy(category = event.category) }
            }

            is ProductEvent.OnClickProductCard -> {
                viewModelScope.launch {
                    onProductCardClick()
                }
            }

            is ProductEvent.OnFilterCategoryClick -> {
                onFilterCategoryClick()
            }

            else -> Unit
        }

    }

    private fun fetchProductRemote() {
        viewModelScope.launch {
            try {
                _productRemoteState.update { it.copy(isLoading = true) }
                productRepository.fetchProduct(page = Page, perPage = PerPage)
            } catch (failure: Failures) {
                _productPagedEvent.send(UiEvent.ShowSnackBar(mapFailureMessage(failure)))
            } catch (e: Exception) {
                _productPagedEvent.send(UiEvent.ShowSnackBar(e.message ?: "Unknown Error"))
            } finally {
                _productRemoteState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun fetchProductPaging() {
        pagingJob?.cancel()
        pagingJob = viewModelScope.launch {
            productRepository.getProducts().cachedIn(viewModelScope)
                .onStart {
                    _productPagedState.update { it.copy(isLoading = true) }
                }.onEach { pagingData ->

                    _productPagedState.update { it.copy(products = pagingData, isLoading = false) }
                }.catch {
                    when (it) {
                        is Failures -> _productPagedEvent.send(
                            UiEvent.ShowSnackBar(
                                mapFailureMessage(it)
                            )
                        )

                        else -> _productPagedEvent.send(
                            UiEvent.ShowSnackBar(
                                it.message ?: "Unknown Error"
                            )
                        )
                    }
                }.launchIn(this)
        }
    }

    private fun onProductCardClick() {
        viewModelScope.launch {
            try {
                _productDetailsState.update { it.copy(isLoading = true) }
                _productPagedEvent.send(
                    UiEvent.Navigation.ProductDetails(
                        destinationId = R.id.productDetailsFragment,
                        args = productDetailsState.value.product
                    )
                )
            } catch (e: Exception) {
                _productPagedEvent.send(UiEvent.ShowSnackBar(e.message ?: "Unknown Error"))
            } finally {
                _productDetailsState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onFilterCategoryClick() {
        productRepository.getProducts()
            .map { pagingData ->
                val selected = productPagedState.value.category
                if (selected.isEmpty()) {
                    pagingData
                } else {
                    pagingData.filter { product ->
                        product.categories.any {
                            selected.contains(it.categoryIdJson)
                        }
                    }
                }
            }
            .cachedIn(viewModelScope)
            .onStart {
                _productPagedState.update { it.copy(isLoading = true) }
            }
            .onEach { filteredPagingData ->
                _productPagedState.update {
                    it.copy(
                        products = filteredPagingData,
                        isLoading = false
                    )
                }
            }
            .catch {
                _productPagedEvent.send(
                    UiEvent.ShowSnackBar(it.message ?: Unknown_Error)
                )
            }
            .launchIn(viewModelScope)

    }


}