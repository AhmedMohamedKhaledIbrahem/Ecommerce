package com.example.ecommerce.features.address.presentation.screen.address.addressrecyclerview

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity

class AddressViewHolder(
    view: View,
    private val onDeleteClickListener: (CustomerAddressEntity) -> Unit,
    private val onEditClickListener: (CustomerAddressEntity) -> Unit,
    private val onCardClickListener: () -> Unit,
) : RecyclerView.ViewHolder(view) {
    private val context = view.context
    private val addressCardView: CardView = itemView.findViewById(R.id.addressCardView)
    private val fullNameAddressTextView: TextView =
        itemView.findViewById(R.id.fullNameAddressTextView)
    private val emailAddressTextView: TextView = itemView.findViewById(R.id.emailAddressTextView)
    private val phoneNumberTextView: TextView = itemView.findViewById(R.id.phoneNumberTextView)
    private val streetAddressTextView: TextView = itemView.findViewById(R.id.streetAddressTextView)
    private val countryTextView: TextView = itemView.findViewById(R.id.countryTextView)
    private val cityTextView: TextView = itemView.findViewById(R.id.cityTextView)
    private val postCodeTextView: TextView = itemView.findViewById(R.id.postCodeTextView)
    private val editAddressTextView: ImageView = itemView.findViewById(R.id.editAddressImageView)
    private val removeAddressTextView: ImageView =
        itemView.findViewById(R.id.deleteAddressImageView)

    @SuppressLint("SetTextI18n")
    fun bind(addressItem: CustomerAddressEntity) {
        fullNameAddressTextView.text =
            context.getString(R.string.fullname) + addressItem.firstName + " " + addressItem.lastName
        emailAddressTextView.text = context.getString(R.string.email) + ":" + addressItem.email
        phoneNumberTextView.text =
            context.getString(R.string.phone_number) + ":" + addressItem.phone
        streetAddressTextView.text = context.getString(R.string.address) + ":" + addressItem.address
        countryTextView.text = context.getString(R.string.country) + addressItem.country
        cityTextView.text = context.getString(R.string.city) + ":" + addressItem.city
        postCodeTextView.text = context.getString(R.string.post_code) + ":" + addressItem.zipCode
        addressCardView.setOnClickListener {
            onCardClickListener()
        }
        editAddressTextView.setOnClickListener {
            onEditClickListener(addressItem)
        }
        removeAddressTextView.setOnClickListener {
            onDeleteClickListener(addressItem)
        }


    }

}