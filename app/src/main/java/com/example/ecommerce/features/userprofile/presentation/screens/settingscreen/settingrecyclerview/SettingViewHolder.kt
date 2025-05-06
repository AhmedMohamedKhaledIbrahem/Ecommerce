package com.example.ecommerce.features.userprofile.presentation.screens.settingscreen.settingrecyclerview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ItemSettingBinding
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingViewHolder(binding: ItemSettingBinding) : RecyclerView.ViewHolder(binding.root) {
    val settingCardView:CardView = binding.settingCardView
    val settingImage: ImageView = binding.settingItemImage
    val settingTitle: TextView = binding.settingItemTitle
    val settingSwitch: MaterialSwitch = binding.settingItemSwitch

}