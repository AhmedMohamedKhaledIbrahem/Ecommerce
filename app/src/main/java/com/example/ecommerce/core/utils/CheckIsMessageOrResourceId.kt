package com.example.ecommerce.core.utils

import android.content.Context
import android.view.View
import com.example.ecommerce.core.ui.event.UiEvent


fun checkIsMessageOrResourceId(event: UiEvent, context: Context,root: View){
    when(event){
        is UiEvent.ShowSnackBar -> {
            if (event.message != "" && event.resId == -1) {
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = event.message
                )
            }else{
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = context.getString(event.resId)
                )
            }
        }
        else -> Unit
    }
}