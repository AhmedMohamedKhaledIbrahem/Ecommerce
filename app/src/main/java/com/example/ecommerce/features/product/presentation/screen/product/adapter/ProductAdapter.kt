package com.example.ecommerce.features.product.presentation.screen.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.ecommerce.core.database.data.entities.relation.ProductWithAllDetails
import com.example.ecommerce.databinding.ItemProductBinding

class ProductAdapter(
    private val onItemClick: (ProductWithAllDetails) -> Unit
) : PagingDataAdapter<ProductWithAllDetails, ProductViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding, onItemClick)
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
