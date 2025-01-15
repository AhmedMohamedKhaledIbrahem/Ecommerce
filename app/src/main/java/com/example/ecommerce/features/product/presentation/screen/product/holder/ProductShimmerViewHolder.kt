package com.example.ecommerce.features.product.presentation.screen.product.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.google.android.material.imageview.ShapeableImageView

class ProductShimmerViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val imageProductShimmer: ShapeableImageView = itemView.findViewById(R.id.imageProductShimmer)
    val priceProductShimmerText: TextView = itemView.findViewById(R.id.priceProductShimmerText)
    val nameProductShimmerText: TextView = itemView.findViewById(R.id.nameProductShimmerText)

}