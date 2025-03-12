package com.example.ecommerce.features.orders.presentation.screens.adapter.order_details_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.example.ecommerce.core.viewholder.EmptyViewHolder

class OrderDetailsAdapter(
    private val orderWithItems: List<OrderItemEntity>,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_EMPTY = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EMPTY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.empty_cart, parent, false)
                EmptyViewHolder(view)
            }

            TYPE_ITEM -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_order_details, parent, false)
                OrderDetailsViewHolder(view,context)
            }

            else -> {
                throw IllegalArgumentException("Invalid view type")
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