package com.example.ecommerce.features.product.presentation.screen.product.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.Currency
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails
import com.example.ecommerce.databinding.ItemProductBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView

class ProductViewHolder(
    binding: ItemProductBinding,
    private val onItemClick: (ProductWithAllDetails) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private val productName: TextView = binding.nameProduct
    private val productPrice: TextView = binding.priceProduct
    private val productImage: ShapeableImageView = binding.imageProduct
    private val productCardView: MaterialCardView = binding.productCardView
    private val productCategory: TextView = binding.categoryProduct


    fun bind(product: ProductWithAllDetails) {
        productName.text = product.product.name
        product.categories.forEach {
            productCategory.text = it.categoryName
        }
        productPrice.text = product.product.price.plus(Currency)

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
