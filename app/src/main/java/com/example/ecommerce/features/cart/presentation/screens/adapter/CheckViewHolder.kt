package com.example.ecommerce.features.cart.presentation.screens.adapter

import android.widget.ProgressBar
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
    private val checkOutButtonProgress: ProgressBar = binding.checkOutButtonProgress


    fun bind(totalPrice: Double, loadingState: Boolean) {
        checkOutButtonLoadingState(loadingState)
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

    fun checkOutButtonLoadingState(loadingState: Boolean) {
        if (loadingState) {
            checkOutButtonProgress.visibility = ProgressBar.VISIBLE
            checkOutButton.isEnabled = false
            checkOutButton.text = ""
        } else {
            checkOutButtonProgress.visibility = ProgressBar.GONE
            checkOutButton.isEnabled = true
            checkOutButton.text = itemView.context.getString(R.string.check_out)
        }
    }
}