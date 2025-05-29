package com.example.ecommerce.features.orders.presentation.screens.adapter.order_details_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.example.ecommerce.databinding.ItemProductDetailsBinding

class ProductDetailsAdapter(
    private val orderItem: List<OrderItemEntity>,

    ) : RecyclerView.Adapter<ProductDetailsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDetailsViewHolder {
        val binding =
            ItemProductDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductDetailsViewHolder(binding)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: ProductDetailsViewHolder, position: Int) {
        holder.bind(orderItem)
    }
}