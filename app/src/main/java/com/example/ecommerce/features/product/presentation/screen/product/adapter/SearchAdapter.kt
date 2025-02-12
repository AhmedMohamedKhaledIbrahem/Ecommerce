package com.example.ecommerce.features.product.presentation.screen.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R

class SearchAdapter(private val onSearchQueryChanged: (String) -> Unit) :
    RecyclerView.Adapter<SearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_bar, parent, false)
        return SearchViewHolder(view, onSearchQueryChanged)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {}
}