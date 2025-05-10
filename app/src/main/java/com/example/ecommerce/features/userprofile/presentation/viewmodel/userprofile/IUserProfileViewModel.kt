package com.example.ecommerce.features.userprofile.presentation.viewmodel.userprofile

import com.example.ecommerce.core.ui.state.UiState
import kotlinx.coroutines.flow.SharedFlow

interface IUserProfileViewModel {
    val userProfileState: SharedFlow<UiState<Any>>
    fun getUserProfile()
    fun <T> userProfileUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    )
}