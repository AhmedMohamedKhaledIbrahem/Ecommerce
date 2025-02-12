package com.example.ecommerce.features.product.presentation.screen.product.adapter

import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R

class SearchViewHolder(view: View, private val onSearchQueryChanged: (String) -> Unit) :
    RecyclerView.ViewHolder(view) {
    private val searchEditText: SearchView = view.findViewById(R.id.searchProduct)
    init{
        searchEditText.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { onSearchQueryChanged(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               newText?.let { onSearchQueryChanged(it) }
                return true
            }

        })
    }

}