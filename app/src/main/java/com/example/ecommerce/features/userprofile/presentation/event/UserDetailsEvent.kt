package com.example.ecommerce.features.userprofile.presentation.event

import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserDetailsRequestEntity

sealed class UserDetailsEvent {

    data class UserDetailsUpdateInput(
        val updateUserDetailsRequestEntity: UpdateUserDetailsRequestEntity
    ) : UserDetailsEvent()
    data object LoadUserDetailsCheck : UserDetailsEvent()
    data object UserDetailsUpdateButton : UserDetailsEvent()
}