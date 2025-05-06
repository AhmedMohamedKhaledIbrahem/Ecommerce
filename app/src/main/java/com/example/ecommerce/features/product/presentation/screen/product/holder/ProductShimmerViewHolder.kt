package com.example.ecommerce.features.product.presentation.screen.product.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ItemProductShimmerBinding
import com.google.android.material.imageview.ShapeableImageView

class ProductShimmerViewHolder(binding: ItemProductShimmerBinding): RecyclerView.ViewHolder(binding.root) {
    val imageProductShimmer: ShapeableImageView = binding.imageProductShimmer
    val priceProductShimmerText: TextView = binding.priceProductShimmerText
    val nameProductShimmerText: TextView = binding.nameProductShimmerText

}