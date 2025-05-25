package com.example.ecommerce.features.userprofile.presentation.event

import java.io.File

sealed class ImageProfileEvent {
    sealed class Input : ImageProfileEvent() {
        data class UploadImage(val imageFile: File) : Input()
        data class GetImage(val userId: Int) : Input()
    }

    data object UploadImageProfileButton : ImageProfileEvent()
    data object LoadImageById : ImageProfileEvent()

}