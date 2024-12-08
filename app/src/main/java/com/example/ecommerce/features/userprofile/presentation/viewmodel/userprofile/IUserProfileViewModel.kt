package com.example.ecommerce.features.userprofile.presentation.viewmodel.userprofile

import com.example.ecommerce.core.state.UiState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IUserProfileViewModel {
    val userProfileState: SharedFlow<UiState<Any>>
    fun getUserProfile()
    fun <T> userProfileUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    )
}