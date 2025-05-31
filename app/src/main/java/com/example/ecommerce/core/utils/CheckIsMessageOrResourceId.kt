package com.example.ecommerce.core.utils

import android.content.Context
import android.view.View
import com.example.ecommerce.R
import com.example.ecommerce.core.ui.event.UiEvent


fun checkIsMessageOrResourceId(event: UiEvent, context: Context, root: View) {
    when (event) {
        is UiEvent.ShowSnackBar -> {
            if (event.message != "" && event.resId == -1) {
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = event.message
                )
            } else {
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = context.getString(event.resId)
                )
            }
        }

        else -> Unit
    }
}

fun checkIsMessageOrResourceId(message: String, resId: Int, context: Context, root: View) {
    if (message != "" && resId == -1) {
        SnackBarCustom.showSnackbar(
            view = root,
            message = message
        )
    } else {
        SnackBarCustom.showSnackbar(
            view = root,
            message = context.getString(resId)
        )
    }

}

fun checkIsMessageOrResourceId(event: UiEvent, context: Context): String {
    return when (event) {
        is UiEvent.ShowSnackBar -> {
            when {
                event.message.isNotEmpty() && event.resId == -1 -> event.message
                event.resId != -1 -> context.getString(event.resId)
                else -> context.getString(R.string.unknown_message)
            }
        }

        else -> context.getString(R.string.unsupported_event)
    }
}
