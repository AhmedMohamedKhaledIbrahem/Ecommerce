package com.example.ecommerce

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NavigationViewModel():ViewModel(),INavigationViewModel {
    private val _selectedItem = MutableLiveData<Int>()
    override val selectedItem: LiveData<Int> = _selectedItem
    override fun updateSelectedItem(itemId: Int) {
        _selectedItem.value = itemId
    }
}