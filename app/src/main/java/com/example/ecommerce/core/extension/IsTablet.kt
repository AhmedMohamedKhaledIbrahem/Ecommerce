package com.example.ecommerce.core.extension

import android.content.Context
import android.content.res.Configuration

fun isTablet(context: Context):Boolean {
    val screenLayout = context.resources.configuration.screenLayout
    val screenSize = screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
    return screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE
}