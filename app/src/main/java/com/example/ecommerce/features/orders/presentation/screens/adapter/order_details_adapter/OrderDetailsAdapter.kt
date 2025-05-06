package com.example.ecommerce.features.orders.presentation.screens.adapter.order_details_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.core.constants.InvalidViewType
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.example.ecommerce.core.viewholder.EmptyViewHolder
import com.example.ecommerce.databinding.EmptyScreenBinding
import com.example.ecommerce.databinding.ItemOrderDetailsBinding

class OrderDetailsAdapter(
    private val orderWithItems: List<OrderItemEntity>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_EMPTY = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EMPTY -> {
                val binding =
                    EmptyScreenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmptyViewHolder(binding)
            }

            TYPE_ITEM -> {
                val binding =
                    ItemOrderDetailsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                OrderDetailsViewHolder(binding)
            }

            else -> {
                throw IllegalArgumentException(InvalidViewType)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (orderWithItems.isEmpty()) TYPE_EMPTY else TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return if (orderWithItems.isEmpty()) 1 else orderWithItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (orderWithItems.isNotEmpty() && holder is OrderDetailsViewHolder) {
            holder.bind(orderWithItems[position])
        }

    }
}