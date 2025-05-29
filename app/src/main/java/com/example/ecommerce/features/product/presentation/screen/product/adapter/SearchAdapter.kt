package com.example.ecommerce.features.product.presentation.screen.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ItemCartBinding
import com.example.ecommerce.databinding.ItemSearchBarBinding

class SearchAdapter(private val onSearchQueryChanged: (String) -> Unit) :
    RecyclerView.Adapter<SearchViewHolder>() {
    private var currentQuery: String = ""
    fun updateQuery(query: String) {
        currentQuery = query
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding =
            ItemSearchBarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding, onSearchQueryChanged)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(currentQuery)
    }
}