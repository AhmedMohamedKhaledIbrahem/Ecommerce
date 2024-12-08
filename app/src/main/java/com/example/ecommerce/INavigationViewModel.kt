package com.example.ecommerce

import androidx.lifecycle.LiveData

interface INavigationViewModel {
    val selectedItem: LiveData<Int>
    fun updateSelectedItem(itemId: Int)
}