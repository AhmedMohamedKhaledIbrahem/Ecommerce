package com.example.ecommerce.features.userprofile.presentation.screens.settingscreen.settingrecyclerview

data class SettingItem(
    val imageResId:Int,
    val title:String,
    val enableSwitch:Boolean ,
    var isChecked:Boolean,
    val destinationId:Int,

){
    companion object {
        const val NO_DESTINATION = -1
    }
}
