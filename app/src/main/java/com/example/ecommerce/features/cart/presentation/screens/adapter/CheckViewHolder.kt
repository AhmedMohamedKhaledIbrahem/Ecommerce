package com.example.ecommerce.features.cart.presentation.screens.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.Currency
import com.example.ecommerce.databinding.ItemCheckOutBinding
import com.google.android.material.button.MaterialButton
import java.util.Locale

class CheckViewHolder(
    binding: ItemCheckOutBinding,
    private val onCheckOutClicked: () -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    private val totalPriceTextView: TextView = binding.totalTextView
    private val checkOutButton: MaterialButton = binding.buttonCheckOut


    fun bind(totalPrice: Double, removeLoadingState: Boolean) {
        checkOutButton.isEnabled = !removeLoadingState
        val context = itemView.context
        val formattedText = String.format(
            Locale.getDefault(),
            context.getString(R.string.total_price_format),
            totalPrice,
            Currency
        )
        totalPriceTextView.text = formattedText
        checkOutButton.setOnClickListener {
            onCheckOutClicked.invoke()
        }
    }
}