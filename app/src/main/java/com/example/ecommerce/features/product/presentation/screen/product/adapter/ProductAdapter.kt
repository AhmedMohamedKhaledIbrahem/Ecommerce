package com.example.ecommerce.features.product.presentation.screen.product.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView

class ProductAdapter(
    private val onItemClick: (ProductWithAllDetails) -> Unit
) : PagingDataAdapter<ProductWithAllDetails, ProductViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        product?.let { holder.bind(it) }
    }




    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductWithAllDetails>() {
            override fun areItemsTheSame(
                oldItem: ProductWithAllDetails,
                newItem: ProductWithAllDetails
            ): Boolean {
                return oldItem.product.productIdJson == newItem.product.productIdJson
            }

            override fun areContentsTheSame(
                oldItem: ProductWithAllDetails,
                newItem: ProductWithAllDetails
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
