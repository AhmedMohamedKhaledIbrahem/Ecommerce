package com.example.ecommerce.features.cart.presentation.screens.diff

import androidx.recyclerview.widget.DiffUtil
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity

class CartDiffCallback : DiffUtil.ItemCallback<ItemCartEntity>() {
    override fun areItemsTheSame(
        oldItem: ItemCartEntity,
        newItem: ItemCartEntity
    ): Boolean {
        return oldItem.itemHashKey == newItem.itemHashKey
    }

    override fun areContentsTheSame(
        oldItem: ItemCartEntity,
        newItem: ItemCartEntity
    ): Boolean {
        return oldItem == newItem
    }
}