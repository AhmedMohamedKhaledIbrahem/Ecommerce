package com.example.ecommerce.features.product.presentation.screen.product.adapter

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.databinding.ItemSearchBarBinding

class SearchViewHolder(
    private val binding: ItemSearchBarBinding,
    private val onSearchQueryChanged: (String) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    private val searchEditText: SearchView = binding.searchProduct

    fun bind(query: String) {
        searchEditText.setQuery(query, false)
        searchEditText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    hideKeyboard()
                } else {
                    onSearchQueryChanged(query)
                }
                searchEditText.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                newText?.let {
                    onSearchQueryChanged(it)
                }
                return true
            }

        })




        searchEditText.setOnCloseListener {
            hideKeyboard()
            searchEditText.clearFocus()
            searchEditText.clearAnimation()

            false
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            binding.root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

}