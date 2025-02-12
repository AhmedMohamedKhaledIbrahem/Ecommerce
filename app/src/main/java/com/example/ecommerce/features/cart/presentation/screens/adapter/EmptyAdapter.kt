package com.example.ecommerce.features.cart.presentation.screens.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R

//class EmptyAdapter : RecyclerView.Adapter<EmptyAdapter.EmptyViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmptyViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.empty_cart, parent, false)
//        return EmptyViewHolder(view)
//    }
//    override fun getItemCount(): Int = 1
//    override fun onBindViewHolder(holder: EmptyViewHolder, position: Int) {
//    }
//
//
//
//}
class EmptyViewHolder(private val view: View) : RecyclerView.ViewHolder(view){}