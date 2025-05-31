package com.example.ecommerce.core.utils

import androidx.navigation.NavOptions

fun navigationOption(fragmentId: Int): NavOptions {
    val navOptions = NavOptions.Builder()
        .setPopUpTo(fragmentId, inclusive = true)
        .build()
    return navOptions
}