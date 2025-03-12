package com.example.ecommerce.features.orders.presentation.screens.adapter.order_details_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity

class ProductDetailsAdapter(
    private val orderItem: List<OrderItemEntity>,
    private val context: Context
) : RecyclerView.Adapter<ProductDetailsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDetailsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_product_details, parent, false)
        return ProductDetailsViewHolder(view, context)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: ProductDetailsViewHolder, position: Int) {
        holder.bind(orderItem)
    }
}