package com.example.ecommerce.features.address.presentation.screen.address.addressrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity

class AddressAdapter(
    private val addressItems: List<CustomerAddressEntity>,
    private val onDeleteClickListener: (CustomerAddressEntity) -> Unit,
    private val onEditClickListener: (CustomerAddressEntity) -> Unit,
    private val onCardClickListener: () -> Unit,

    ) : RecyclerView.Adapter<AddressViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        return AddressViewHolder(
            view,
            onDeleteClickListener,
            onEditClickListener,
            onCardClickListener
        )
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = addressItems[position]
        holder.bind(address)

    }


    override fun getItemCount(): Int = addressItems.size

}