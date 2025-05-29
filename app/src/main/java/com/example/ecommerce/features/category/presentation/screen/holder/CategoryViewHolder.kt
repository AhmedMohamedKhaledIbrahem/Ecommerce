package com.example.ecommerce.features.category.presentation.screen.holder

import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.core.database.data.entities.category.CategoryEntity
import com.example.ecommerce.databinding.ItemFilterCategoryBinding

class CategoryViewHolder(
    binding: ItemFilterCategoryBinding,
    private val selectedCategoryIds: Set<Int>,
    private val onFilterCategoryClick: (CategoryEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private val filterCategoryChip = binding.filterCategoryChip
    fun bind(category: CategoryEntity) {
        filterCategoryChip.text = category.name
        filterCategoryChip.isChecked = selectedCategoryIds.contains(category.id)
        filterCategoryChip.setOnClickListener {
            onFilterCategoryClick(category)
        }

    }
}