package com.example.ecommerce.features.userprofile.presentation.state

import com.example.ecommerce.core.database.data.entities.user.UserEntity

data class UserProfileState(
    val  userEntity: UserEntity? = null,
)
