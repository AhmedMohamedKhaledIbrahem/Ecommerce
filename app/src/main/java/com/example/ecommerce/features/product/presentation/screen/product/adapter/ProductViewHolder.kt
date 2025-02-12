package com.example.ecommerce.features.product.presentation.screen.product.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView

class ProductViewHolder(
    itemView: View,
    private val onItemClick: (ProductWithAllDetails) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private val productName: TextView = itemView.findViewById(R.id.nameProduct)
    private val productPrice: TextView = itemView.findViewById(R.id.priceProduct)
    private val productImage: ShapeableImageView = itemView.findViewById(R.id.imageProduct)
    private val productCardView: MaterialCardView = itemView.findViewById(R.id.productCardView)
    private val productCategory: TextView = itemView.findViewById(R.id.categoryProduct)


    @SuppressLint("SetTextI18n")
    fun bind(product: ProductWithAllDetails) {
        productName.text = product.product.name
        product.categories.forEach {
            productCategory.text = it.categoryName
        }
        productPrice.text = "${product.product.price} EG"

        product.images.forEach {
            productImage.load(it.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.round_placeholder_24)
                error(R.drawable.baseline_wifi_off_24)
            }
        }
        productCardView.setOnClickListener {
            onItemClick(product)
        }
    }
}
