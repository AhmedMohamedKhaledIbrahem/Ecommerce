package com.example.ecommerce.features.userprofile.presentation.state

import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserDetailsRequestEntity

data class UserDetailsState(
    val isUpdateLoading: Boolean = false,
    val isUpdateSuccess: Boolean = false,
    val updateUserDetailsRequestEntity: UpdateUserDetailsRequestEntity? = null,
    val displayName: String? = null
)
