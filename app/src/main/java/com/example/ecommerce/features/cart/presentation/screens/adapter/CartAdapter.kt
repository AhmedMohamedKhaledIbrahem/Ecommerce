package com.example.ecommerce.features.cart.presentation.screens.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.InvalidViewType
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity
import com.example.ecommerce.core.viewholder.EmptyViewEntity
import com.example.ecommerce.core.viewholder.EmptyViewHolder
import com.example.ecommerce.databinding.EmptyScreenBinding
import com.example.ecommerce.databinding.ItemCartBinding

class CartAdapter(
    private val cartItems: List<ItemCartEntity>,
    private val onCounterUpdate: (ItemCartEntity, Int) -> Unit,
    private val onDeleteItem: (ItemCartEntity) -> Unit
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
            val emptyMessageLocal = holder.itemView.context.getString(R.string.empty_cart_message)
            val emptyViewEntity = EmptyViewEntity(
                emptyText = emptyMessageLocal,
                emptyImage = R.drawable.empty_cart
            )
            holder.bind(emptyViewEntity)
        } else if (holder is CartViewHolder) {
            holder.bind(cartItems[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> {
                val binding =
                    ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CartViewHolder(binding, onCounterUpdate, onDeleteItem)
            }

            TYPE_EMPTY -> {
                val binding = EmptyScreenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmptyViewHolder(binding)
            }

            else -> {
                throw IllegalArgumentException(InvalidViewType)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (cartItems.isEmpty()) 1 else cartItems.size
    }
}