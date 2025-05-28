package com.example.ecommerce.features.category.presentation.screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.core.database.data.entities.category.CategoryEntity
import com.example.ecommerce.databinding.ItemFilterCategoryBinding
import com.example.ecommerce.features.category.presentation.screen.holder.CategoryViewHolder

class CategoryAdapter(
    private val categories: List<CategoryEntity>,
    private val selectedCategoryIds: Set<Int>,
    private val onFilterCategoryClick: (CategoryEntity) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            ItemFilterCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding, selectedCategoryIds, onFilterCategoryClick)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is CategoryViewHolder) {
            val category = categories[position]
            holder.bind(category)
        }

    }

    override fun getItemCount(): Int = categories.size

}