package com.example.ecommerce.features.product.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ExpandedBottomSheetFilterViewModel : ViewModel() {
    private val _expandedFilter = MutableStateFlow<Boolean>(false)
    val expandedFilter: StateFlow<Boolean> = _expandedFilter.asStateFlow()

    fun setExpandedFilter(expanded: Boolean) {
        _expandedFilter.update { expanded }
    }
}