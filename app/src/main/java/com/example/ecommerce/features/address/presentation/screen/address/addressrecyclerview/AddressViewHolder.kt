package com.example.ecommerce.features.address.presentation.screen.address.addressrecyclerview

import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.databinding.ItemAddressBinding

class AddressViewHolder(
    binding: ItemAddressBinding,
    private val onDeleteClickListener: (CustomerAddressEntity) -> Unit,
    private val onEditClickListener: (CustomerAddressEntity) -> Unit,
    private val onCardClickListener: (CustomerAddressEntity) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private val context = itemView.context
    private val addressCardView = binding.addressCardView
    private val fullNameAddressTextView =
        binding.fullNameAddressTextView
    private val emailAddressTextView = binding.emailAddressTextView
    private val phoneNumberTextView = binding.phoneNumberTextView
    private val streetAddressTextView = binding.streetAddressTextView
    private val countryTextView = binding.countryTextView
    private val cityTextView = binding.cityTextView
    private val postCodeTextView = binding.postCodeTextView
    private val editAddressTextView = binding.editAddressImageView
    private val removeAddressTextView =
        binding.deleteAddressImageView
    private val selectAddressIcon = binding.selectedAddressIcon

    fun bind(addressItem: CustomerAddressEntity) {
        fullNameAddressTextView.text =
            context.getString(R.string.full_name).plus(addressItem.firstName).plus(" ")
                .plus(addressItem.lastName)
        emailAddressTextView.text = context.getString(R.string.email).plus(addressItem.email)
        phoneNumberTextView.text =
            context.getString(R.string.phone_number).plus(addressItem.phone)
        streetAddressTextView.text = context.getString(R.string.address).plus(addressItem.address)
        countryTextView.text = context.getString(R.string.country).plus(addressItem.country)
        cityTextView.text = context.getString(R.string.city).plus(addressItem.city)
        postCodeTextView.text = context.getString(R.string.post_code).plus(addressItem.zipCode)

        if (addressItem.isSelect == 1) {
            selectAddressIcon.visibility = RecyclerView.VISIBLE
        } else {
            selectAddressIcon.visibility = RecyclerView.GONE
        }
        addressCardView.setOnClickListener {
            onCardClickListener(addressItem)
        }
        editAddressTextView.setOnClickListener {
            onEditClickListener(addressItem)
        }
        removeAddressTextView.setOnClickListener {
            onDeleteClickListener(addressItem)
        }


    }

}