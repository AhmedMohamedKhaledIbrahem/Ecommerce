package com.example.ecommerce.features.product.presentation.screen.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.features.product.presentation.screen.product.holder.ProductShimmerViewHolder
import com.example.ecommerce.features.product.presentation.screen.product.item.ProductShimmerItem

class ProductShimmerAdapter(
    private val shimmerItems: List<ProductShimmerItem>,
) : RecyclerView.Adapter<ProductShimmerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductShimmerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_shimmer, parent, false)
        return ProductShimmerViewHolder(view)
    }

    override fun getItemCount(): Int = shimmerItems.size

    override fun onBindViewHolder(holder: ProductShimmerViewHolder, position: Int) {
        val productShimmer = shimmerItems[position]
        holder.imageProductShimmer.setImageDrawable(null)
        holder.nameProductShimmerText.text = productShimmer.productName
        holder.priceProductShimmerText.text = productShimmer.productPrice

    }
}