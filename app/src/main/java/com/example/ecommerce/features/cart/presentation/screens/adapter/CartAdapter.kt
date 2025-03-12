package com.example.ecommerce.features.cart.presentation.screens.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity
import com.example.ecommerce.core.viewholder.EmptyViewHolder

class CartAdapter(
    private val cartItems: List<ItemCartEntity>,
    private val onCounterUpdate: (ItemCartEntity, Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_EMPTY = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (cartItems.isEmpty()) TYPE_EMPTY else TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (cartItems.isEmpty() && holder is EmptyViewHolder) {
        } else if (holder is CartViewHolder) {
            holder.bind(cartItems[position]) // Ensure `position` is valid
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val create = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
//        return CartViewHolder(create, onCounterUpdate)
        return when (viewType) {
            TYPE_ITEM -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
                CartViewHolder(view, onCounterUpdate)
            }
            TYPE_EMPTY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.empty_cart, parent, false)
                EmptyViewHolder(view)
            }

            else -> {throw IllegalArgumentException("Invalid view type")}
        }
    }

    override fun getItemCount(): Int {
        return if (cartItems.isEmpty()) 1 else cartItems.size
    }
}