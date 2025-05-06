package com.example.ecommerce.features.userprofile.presentation.viewmodel.usernamedetailsprofile

import com.example.ecommerce.core.ui.UiState
import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserNameDetailsRequestEntity
import kotlinx.coroutines.flow.SharedFlow

interface IUserNameDetailsProfileViewModel {
    val userNameDetailsProfileState: SharedFlow<UiState<Any>>
    fun getUserNameDetails()
    fun updateUserNameDetails(updateUserNameDetailsParams: UpdateUserNameDetailsRequestEntity)
    fun <T> userNameDetailsProfileUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source:String
    )


}