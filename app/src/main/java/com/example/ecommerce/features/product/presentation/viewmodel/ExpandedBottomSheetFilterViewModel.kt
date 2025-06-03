package com.example.ecommerce.features.product.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ExpandedBottomSheetFilterViewModel : ViewModel() {
    private val _expandedFilter: Channel<Boolean> = Channel()
    val expandedFilter = _expandedFilter.receiveAsFlow()

    fun setExpandedFilter(expanded: Boolean) {
        viewModelScope.launch {
            _expandedFilter.send(expanded)
        }
    }
}