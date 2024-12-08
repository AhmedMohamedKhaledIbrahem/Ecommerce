package com.example.ecommerce.features.userprofile.presentation.screens.settingscreen.settingrecyclerview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val settingCardView:CardView = itemView.findViewById(R.id.settingCardView)
    val settingImage: ImageView = itemView.findViewById(R.id.settingItemImage)
    val settingTitle: TextView = itemView.findViewById(R.id.settingItemTitle)
    val settingSwitch: MaterialSwitch = itemView.findViewById(R.id.settingItemSwitch)

}