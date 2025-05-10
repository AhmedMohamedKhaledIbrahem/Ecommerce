package com.example.ecommerce.features.userprofile.presentation.viewmodel.imageprofile

import com.example.ecommerce.core.ui.state.UiState
import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface IImageProfileViewModel {
    val imageProfileState: StateFlow<UiState<Any>>
     fun uploadImageProfile(image: File)
     fun getImageProfileById(userId: Int)
    fun <T> imageProfileUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source:String
    )
}