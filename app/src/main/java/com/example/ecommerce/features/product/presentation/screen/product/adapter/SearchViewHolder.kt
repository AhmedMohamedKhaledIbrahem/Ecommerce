package com.example.ecommerce.features.product.presentation.screen.product.adapter

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R

class SearchViewHolder(private val view: View, private val onSearchQueryChanged: (String) -> Unit) :
    RecyclerView.ViewHolder(view) {
    private val searchEditText: SearchView = view.findViewById(R.id.searchProduct)

    fun bind(query: String) {
        searchEditText.setQuery(query, false)
        searchEditText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    hideKeyboard(view)
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
            hideKeyboard(view)
            searchEditText.clearFocus()
            searchEditText.clearAnimation()

            false
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}